package cc.sifter

import java.util.Random

abstract class BaseBandit(arms : Seq[Arm]) {

  lazy val rand = new Random()
  lazy val armCount = arms.size
  protected val armIndexFromId = Map[String, Int]((0 until armCount).map(i => arms(i).getId -> i):_*)
  protected def isValidId(id: String) : Boolean = armIndexFromId.keySet.contains(id)
  
  /**
   * Method to choose which arm will be returned to the user.
   */
  protected def chooseArm() : Arm

  /**
   * User-facing method for selecting the next arm to use.
   */
  def selectArm() : Selection = {
    val arm = chooseArm()
    arm.incrementRequestCount()
    Selection(arm.getId)
  }

  // TODO: docs!
  def update(selection : Selection) : Boolean = {
    require( isValidId(selection.id) )
    var arm : Arm = arms(armIndexFromId(selection.id))
    arm.incrementPullCount
    updateAlgorithm(arm, selection.value)
  }

  protected def updateAlgorithm(arm : Arm, value : Double) : Boolean

  def printInfo() {
    println("Arms:   " + arms.map(i=>i.getId).mkString(", "))
    println("Pulls:  " + arms.map(i=>i.getPullCount.toString).mkString(", "))
    println("Values: " + arms.map(i=>i.getValue.toString).mkString(", "))
  }

  protected def getMaxArm : Arm = {
    var maxValue = arms(0).getValue
    var maxIndex = 0
    for (i <- 1 until armCount) {
      if (arms(i).getValue > maxValue) { maxIndex = i; maxValue = arms(i).getValue }
    }
    arms(maxIndex)
  }

  protected def totalValue : Double = arms.map(i=>i.getValue).sum

  // TODO: docs
  def categoricalDraw(probs : Seq[Double]) : Int = {
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
