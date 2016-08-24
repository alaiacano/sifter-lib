package cc.sifter

import java.util.Random
import scala.collection.mutable.{Map => MMap}

trait Bandit {

  val arms: Seq[Arm]
  val armsMap: MMap[String, Arm] = MMap(arms.map(arm => arm.id -> arm): _*)

  lazy val rand = new Random()

  /**
   * Method to choose which arm will be returned to the user.
   */
  protected def chooseArm(): Arm

  /**
   * User-facing method for selecting the next arm to use.
   */
  def selectArm(): Selection = {
    val arm = chooseArm()
    armsMap(arm.id) = arm.incrementRequestCount()
    Selection(arm.id)
  }

  def update(selection: Selection): Bandit = {
    val updatedArm: Arm = armsMap
      .getOrElse(selection.id, throw new Exception(s"Unknown arm: ${selection.id}"))
      .incrementPullCount()

    updateAlgorithm(updatedArm, selection.value)
  }

  protected def updateAlgorithm(arm: Arm, value: Double): Bandit

  def maxArm: Arm = armsMap.values.maxBy(_.value)

  protected def totalValue: Double = armsMap.values.map(_.value).sum

  def categoricalDraw(probs: Seq[Double]): Int = {
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
}
