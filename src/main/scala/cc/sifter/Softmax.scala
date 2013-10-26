package cc.sifter

abstract class BaseSoftMax(arms: Seq[Arm]) extends BaseBandit(arms) {

  override protected def totalValue: Double = arms.map(i => math.exp(i.value/ getTemperature)).sum

  def getTemperature: Double

  def chooseArm(): Arm = {
    val probs = arms.map(a => math.exp(a.value / getTemperature) / totalValue) 
    arms(categoricalDraw(probs))
  }

  def updateAlgorithm(arm: Arm, value: Double): Boolean = {
    try {
      val n = arm.pullCount.toDouble
      val oldValue = arm.value
      arm.value = ((n - 1) / n) * oldValue + (1 / n) * value
      true
    }
    catch {
      case _ => false
    }
  }
}


object SoftMax {
  def apply(arms: Seq[Arm], temperature: Double) = new SoftMax(arms, temperature)
}

class SoftMax(val arms: Seq[Arm], val temperature: Double) extends BaseSoftMax(arms) {
  override def getTemperature: Double = temperature
}

object AnnealingSoftMax {
  def apply(arms: Seq[Arm]) = new AnnealingSoftMax(arms)
}

class AnnealingSoftMax(val arms: Seq[Arm]) extends BaseSoftMax(arms) {
  override def getTemperature: Double = 1.0 / math.log(0.0000001 + arms.map(a=>a.pullCount).sum)
}
