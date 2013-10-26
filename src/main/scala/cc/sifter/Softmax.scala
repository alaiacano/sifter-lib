package cc.sifter

abstract class BaseSoftMax(arms: Seq[Arm]) extends BaseBandit(arms) {

  def temperature: Double

  override protected def totalValue: Double = arms.map(i => math.exp(i.value/ temperature)).sum

  def chooseArm(): Arm = {
    val probs = arms.map(a => math.exp(a.value / temperature) / totalValue) 
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


case class SoftMax(val arms: Seq[Arm], temp: Double) extends BaseSoftMax(arms) {
  override def temperature: Double = temp
}

case class AnnealingSoftMax(val arms: Seq[Arm]) extends BaseSoftMax(arms) {
  override def temperature: Double = 1.0 / math.log(0.0000001 + arms.map(a=>a.pullCount).sum)
}
