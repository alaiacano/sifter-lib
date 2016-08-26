package cc.sifter

import scala.collection.mutable.{Map => MMap}

trait BaseSoftMax extends Bandit {

  def temperature: Double

  override def totalValue: Double = armsMap.values.map(i => math.exp(i.value/ temperature)).sum

  def chooseArm(): Arm = {
    val probs: Seq[Double] = armsMap.values.map(a => math.exp(a.value / temperature) / totalValue).toSeq
    armsMap.values.toIndexedSeq(categoricalDraw(probs))
  }

  override def updateAlgorithm(arm: Arm, value: Double): Unit = {
    val n = arm.pullCount.toDouble
    val oldValue = arm.value
    val updatedArm = arm.copy(value = ((n - 1) / n) * oldValue + (1 / n) * value)
    armsMap(arm.id) = updatedArm
  }
}

case class SoftMax(initArms: Seq[Arm], temp: Double) extends BaseSoftMax {
  val armsMap = MMap(initArms.map(arm => arm.id -> arm):_*)
  def temperature: Double = temp
}

case class AnnealingSoftMax(initArms: Seq[Arm]) extends BaseSoftMax {
  val armsMap = MMap(initArms.map(arm => arm.id -> arm):_*)
  def temperature: Double = 1.0 / math.log(0.0000001 + arms.map(a=>a.pullCount).sum)
}
