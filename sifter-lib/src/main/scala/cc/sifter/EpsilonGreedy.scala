package cc.sifter

import scala.util.Try

trait BaseEpsilonGreedy extends BaseBandit {

  def epsilon: Double

  def chooseArm(): Arm = {
    if (rand.nextDouble() < epsilon) {
      getMaxArm
    }
    else {
      arms(rand.nextInt(armCount))
    }
  }

  protected def updateAlgorithm(arm: Arm, value: Double): Option[Boolean] = {
    Try{
      arm.incrementValue(value)
      true
    }.toOption
  }
}


case class AnnealingEpsilonGreedy(arms: Seq[Arm]) extends BaseEpsilonGreedy {
  def epsilon: Double = {
    val totalTests: Double = arms.map(arm => arm.pullCount).sum + 1
    1 / math.log(totalTests + 0.0000001)
  }
}


case class EpsilonGreedy(arms: Seq[Arm], eps: Double) extends BaseEpsilonGreedy {
  override def epsilon: Double = eps
}
