package cc.sifter

object Exp3 {
  def apply(arms: Seq[Arm], gamma: Double) = {
    arms.map( i => i.setValue(1.0) )
    new Exp3(arms, gamma)
  }
  def apply(arms: Seq[Arm]) = {
    arms.map( i => i.setValue(1.0) )
    new Exp3(arms, .5)
  }
}

class Exp3(val arms: Seq[Arm], val gamma: Double) extends BaseBandit(arms) {

  protected def chooseArm(): Arm = {
    val probs = arms.map(i => (1.0 - gamma) * (i.getValue / totalValue) + gamma / armCount)
    arms(categoricalDraw(probs))
  }
  
  private def calculateGrowthFactor(value: Double) = math.exp(((1 - gamma) * value / totalValue) * gamma / arms.size)

  protected def updateAlgorithm(arm: Arm, value: Double): Boolean = {
    try {
      arm.setValue(arm.getValue * calculateGrowthFactor(value))
      true
    }
    catch {
      case _ => false
    }
  }
}
