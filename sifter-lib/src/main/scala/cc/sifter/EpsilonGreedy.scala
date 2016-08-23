package cc.sifter

trait BaseEpsilonGreedy extends Bandit {

  def epsilon: Double

  def chooseArm(): Arm = {
    if (rand.nextDouble() < epsilon) {
      maxArm
    }
    else {
      armsMap.values.toIndexedSeq(rand.nextInt(armsMap.size))
    }
  }
}


case class AnnealingEpsilonGreedy(arms: Seq[Arm]) extends Bandit with BaseEpsilonGreedy{
  def epsilon: Double = {
    val totalTests: Double = arms.map(arm => arm.pullCount).sum + 1
    1 / math.log(totalTests + 0.0000001)
  }

  def updateAlgorithm(arm: Arm, value: Double): AnnealingEpsilonGreedy = {
    val updatedArm = arm.copy(value = value + arm.value)
    val newArms = arms.map(a => if (a.id == updatedArm.id) updatedArm else a)
    this.copy(arms=newArms)
  }
}

case class EpsilonGreedy(arms: Seq[Arm], eps: Double) extends BaseEpsilonGreedy {
  val epsilon: Double = eps

  def updateAlgorithm(arm: Arm, value: Double): EpsilonGreedy = {
    val updatedArm = arm.copy(value = value + arm.value)
    val newArms = arms.map(a => if (a.id == updatedArm.id) updatedArm else a)
    this.copy(arms = newArms)
  }
}
