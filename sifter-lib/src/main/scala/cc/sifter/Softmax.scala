package cc.sifter

trait BaseSoftMax extends Bandit {

  def temperature: Double

  val arms: Seq[Arm]

  override protected def totalValue: Double = armsMap.values.map(i => math.exp(i.value/ temperature)).sum

  def chooseArm(): Arm = {
    val probs: Seq[Double] = armsMap.values.map(a => math.exp(a.value / temperature) / totalValue).toSeq
    armsMap.values.toIndexedSeq(categoricalDraw(probs))
  }
}


case class SoftMax(arms: Seq[Arm], temp: Double) extends BaseSoftMax {
  def temperature: Double = temp

  def updateAlgorithm(arm: Arm, value: Double): SoftMax = {
    val n = arm.pullCount.toDouble
    val oldValue = arm.value
    val updatedArm = arm.copy(value = ((n - 1) / n) * oldValue + (1 / n) * value)
    val newArms = arms.map(a => if (a.id == updatedArm.id) updatedArm else a)
    this.copy(arms=newArms)
  }
}

case class AnnealingSoftMax(arms: Seq[Arm]) extends BaseSoftMax {
  def temperature: Double = 1.0 / math.log(0.0000001 + arms.map(a=>a.pullCount).sum)

  def updateAlgorithm(arm: Arm, value: Double): AnnealingSoftMax = {
    val n = arm.pullCount.toDouble
    val oldValue = arm.value
    val updatedArm = arm.copy(value = ((n - 1) / n) * oldValue + (1 / n) * value)
    val newArms = arms.map(a => if (a.id == updatedArm.id) updatedArm else a)
    this.copy(arms=newArms)
  }
}
