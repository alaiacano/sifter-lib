package cc.sifter

abstract class BaseEpsilonGreedy(arms : Seq[Arm]) extends BaseBandit(arms) {

  def calculateEpsilon() : Double

  def chooseArm() : Arm = {
    val epsilon = calculateEpsilon()
    if (rand.nextDouble() < epsilon) {
      getMaxArm
    }
    else {
      arms(rand.nextInt(armCount))
    }
  }

  def recordSuccess(arm : Arm, value : Double = 1.0) : Boolean = {
    try {
      arm.incrementValue(value)
      true
    }
    catch {
      case _ => false
    }
  }

  def recordFailure(arm : Arm, value : Double = 1.0) : Boolean = {
    try {
      arm.decrementValue(value)
      true
    }
    catch {
      case _ => false
    }
  }

}


object AnnealingEpsilonGreedy {
  def apply(arms: Seq[Arm]) = {
    new AnnealingEpsilonGreedy(arms)
  }
}

class AnnealingEpsilonGreedy(val arms : Seq[Arm]) extends BaseEpsilonGreedy(arms) {
  def calculateEpsilon() : Double = {
    val totalTests : Double = arms.map(arm => arm.getPullCount).sum + 1
    1 / math.log(totalTests + 0.0000001)
  }
}

object EpsilonGreedy {
  def apply(arms: Seq[Arm], eps : Double) = {
    new EpsilonGreedy(arms, eps)
  }
}


class EpsilonGreedy(val arms : Seq[Arm], epsilon : Double) extends BaseEpsilonGreedy(arms) {
  override def calculateEpsilon() : Double = epsilon
}
