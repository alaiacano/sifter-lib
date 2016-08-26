package cc.sifter

import scala.collection.mutable.{Map => MMap}

object Exp3 {

  def apply(arms: Seq[Arm], gamma: Double): Exp3 = {
    val resetValues = arms.map( arm => arm.copy(value = 1.0))
    new Exp3(resetValues, gamma)
  }

  def apply(arms: Seq[Arm]): Exp3 = {
    val resetValues = arms.map( arm => arm.copy(value = 1.0))
    new Exp3(resetValues, .5)
  }
}

class Exp3(initArms: Seq[Arm], val gamma: Double) extends Bandit {
  val armsMap = MMap(initArms.map(arm => arm.id -> arm):_*)

  protected def chooseArm(): Arm = {
    val probs = arms.map(i => (1.0 - gamma) * (i.value / totalValue) + gamma / arms.size.toDouble)
    arms(categoricalDraw(probs))
  }
  
  private def calculateGrowthFactor(value: Double): Double = math.exp(((1 - gamma) * value / totalValue) * gamma / arms.size)

  override def updateAlgorithm(arm: Arm, value: Double) = {
    val updatedArm = arm.copy(value = arm.value * calculateGrowthFactor(value))
    armsMap(updatedArm.id) = updatedArm
  }
}
