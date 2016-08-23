package cc.sifter

import scala.util.Try

trait BaseSoftMax extends BaseBandit {

  def temperature: Double

  override protected def totalValue: Double = arms.map(i => math.exp(i.value/ temperature)).sum

  def chooseArm(): Arm = {
    val probs = arms.map(a => math.exp(a.value / temperature) / totalValue) 
    arms(categoricalDraw(probs))
  }

  def updateAlgorithm(arm: Arm, value: Double): Option[Boolean] = {
    Try {
      val n = arm.pullCount.toDouble
      val oldValue = arm.value
      arm.value = ((n - 1) / n) * oldValue + (1 / n) * value
      true
    }.toOption
  }
}


case class SoftMax(arms: Seq[Arm], temp: Double) extends BaseSoftMax {
  def temperature: Double = temp
}

case class AnnealingSoftMax(arms: Seq[Arm]) extends BaseSoftMax {
  def temperature: Double = 1.0 / math.log(0.0000001 + arms.map(a=>a.pullCount).sum)
}
