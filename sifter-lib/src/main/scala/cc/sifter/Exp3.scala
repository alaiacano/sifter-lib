package cc.sifter

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

class Exp3(val arms: Seq[Arm], val gamma: Double) extends Bandit {

  protected def chooseArm(): Arm = {
    val probs = arms.map(i => (1.0 - gamma) * (i.value / totalValue) + gamma / arms.size.toDouble)
    arms(categoricalDraw(probs))
  }
  
  private def calculateGrowthFactor(value: Double): Double = math.exp(((1 - gamma) * value / totalValue) * gamma / arms.size)

  def updateAlgorithm(arm: Arm, value: Double): Exp3 = {
    val updatedArm = arm.copy(value = value + arm.value)
    val newArms = arms.map(a => if (a.id == updatedArm.id) updatedArm else a)
    new Exp3(newArms, gamma)
  }
}
