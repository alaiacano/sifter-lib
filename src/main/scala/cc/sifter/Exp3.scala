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

class Exp3(val arms : Seq[Arm], val gamma : Double) extends BaseBandit(arms) {

  def categoricalDraw(probs : Seq[Double]) : Int = {
    val z = rand.nextDouble()
    var cumProb = 0.0
    val loop = (0 to probs.size).toIterator
    var i = 0
    while (cumProb < z ) {
      i = loop.next
      cumProb += probs(i)
    }
    i
  }

  protected def chooseArm() : Arm = {
    val probs = arms.map(i => (1.0 - gamma) * (i.getValue / totalValue) + gamma / armCount)
    arms(categoricalDraw(probs))
  }

  
  private def calculateGrowthFactor(value : Double) = math.exp(((1 - gamma) * value / totalValue) * gamma / arms.size)

  protected def recordSuccess(arm : Arm, value : Double) : Boolean = {
    try {
      val v = math.abs(value)
      arm.setValue(arm.getValue * calculateGrowthFactor(v))
      true
    }
    catch {
      case _ => false
    }
  }

  protected def recordFailure(arm : Arm, value : Double) : Boolean = {
    try {
      val v = -1 * math.abs(value)
      arm.setValue(arm.getValue * calculateGrowthFactor(v))
      true
    }
    catch {
      case _ => false
    }
  }
}
