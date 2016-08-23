package cc.sifter

import scala.util.Try

object Exp3 {
  def apply(arms: Seq[Arm], gamma: Double) = {
    arms.map( i => i.value = 1.0 )
    new Exp3(arms, gamma)
  }
  def apply(arms: Seq[Arm]) = {
    arms.map( i => i.value = 1.0 )
    new Exp3(arms, .5)
  }
}

class Exp3(val arms: Seq[Arm], gamma: Double) extends BaseBandit {

  protected def chooseArm(): Arm = {
    val probs = arms.map(i => (1.0 - gamma) * (i.value / totalValue) + gamma / armCount)
    arms(categoricalDraw(probs))
  }
  
  private def calculateGrowthFactor(value: Double) = math.exp(((1 - gamma) * value / totalValue) * gamma / arms.size)

  protected def updateAlgorithm(arm: Arm, value: Double): Option[Boolean] = {
    Try {
      arm.value = arm.value * calculateGrowthFactor(value)
      true
    }.toOption
  }
}
