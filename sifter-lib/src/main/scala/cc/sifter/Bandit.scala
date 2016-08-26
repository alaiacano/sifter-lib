package cc.sifter

import java.util.Random
import scala.collection.mutable.{Map => MMap}

trait Bandit {

  /**
    * This is the mutable state for the bandit.
    */
  val armsMap: MMap[String, Arm]
  def arm(id: String): Option[Arm] = armsMap.get(id)

  def arms: Seq[Arm] = armsMap.values.toSeq

  val rand = new Random()

  /**
    * Method to choose which [[Arm]] will be returned to the user. Each algorithm
    * will have its own selection function.
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

  def update(selection: Selection): Unit = {
    val updatedArm: Arm = armsMap
      .getOrElse(selection.id, throw new Exception(s"Unknown arm: ${selection.id}"))
      .incrementPullCount()

    updateAlgorithm(updatedArm, selection.value)
  }

  /**
    * This is what needs to be extended to update the algorithm. It will likely require
    * replacing the arm in the internal state with the one provided here and whatever else
    * you need to do.
    *
    * @param arm The [[Arm]] that the user is reporting back.
    * @param value The value that the user is reporting back.
    * @return A new instance of a [[Bandit]].
    */
  protected def updateAlgorithm(arm: Arm, value: Double): Unit = {
    val updatedArm = arm.copy(value = value + arm.value)
    armsMap(updatedArm.id) = updatedArm
  }

  def maxArm: Arm = armsMap.values.maxBy(_.value)

  def totalValue: Double = armsMap.values.map(_.value).sum

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
