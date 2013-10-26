package cc.sifter

abstract class BaseEpsilonGreedy(arms: Seq[Arm]) extends BaseBandit(arms) {

  def epsilon: Double

  def chooseArm(): Arm = {
    if (rand.nextDouble() < epsilon) {
      getMaxArm
    }
    else {
      arms(rand.nextInt(armCount))
    }
  }

  protected def updateAlgorithm(arm: Arm, value: Double): Boolean = {
    try {
      arm.incrementValue(value)
      true
    }
    catch {
      case _ => false
    }
  }
}


case class AnnealingEpsilonGreedy(val arms: Seq[Arm]) extends BaseEpsilonGreedy(arms) {
  def epsilon: Double = {
    val totalTests: Double = arms.map(arm => arm.pullCount).sum + 1
    1 / math.log(totalTests + 0.0000001)
  }
}


case class EpsilonGreedy(val arms: Seq[Arm], eps: Double) extends BaseEpsilonGreedy(arms) {
  override def epsilon: Double = eps
}
