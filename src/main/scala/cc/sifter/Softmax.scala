package cc.sifter

abstract class BaseSoftMax(arms: Seq[Arm]) extends BaseBandit(arms) {

  override protected def totalValue : Double = arms.map(i => math.exp(i.getValue/ getTemperature)).sum

  def getTemperature : Double

  def chooseArm() : Arm = {
    val probs = arms.map(a => math.exp(a.getValue / getTemperature) / totalValue) 
    arms(categoricalDraw(probs))
  }

  def updateAlgorithm(arm: Arm, value: Double) : Boolean = {
    try {
      val n = arm.getPullCount.toDouble
      val oldValue = arm.getValue
      arm.setValue(((n - 1) / n) * oldValue + (1 / n) * value)
      true
    }
    catch {
      case _ => false
    }
  }
}


object SoftMax {
  def apply(arms : Seq[Arm], temperature : Double) = new SoftMax(arms, temperature)
}

class SoftMax(arms : Seq[Arm], temperature : Double) extends BaseSoftMax(arms) {
  override def getTemperature : Double = temperature
}

class AnnealingSoftMax(arms : Seq[Arm]) extends BaseSoftMax(arms) {
  override def getTemperature : Double = 1.0 / math.log(0.0000001 + arms.map(a=>a.getPullCount).sum)
}