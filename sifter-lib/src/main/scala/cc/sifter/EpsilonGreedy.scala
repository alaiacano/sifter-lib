package cc.sifter

import collection.mutable.{Map => MMap}

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


case class AnnealingEpsilonGreedy(initArms: Seq[Arm]) extends Bandit with BaseEpsilonGreedy{
  val armsMap = MMap(initArms.map(a => a.id -> a):_*)
  def epsilon: Double = {
    val totalTests: Double = armsMap.values.map(arm => arm.pullCount).sum + 1.0
    1 / math.log(totalTests + 0.0000001)
  }
}

case class EpsilonGreedy(initArms: Seq[Arm], eps: Double) extends BaseEpsilonGreedy {
  val armsMap = MMap(initArms.map(a => a.id -> a):_*)
  val epsilon: Double = eps
}
