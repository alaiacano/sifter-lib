package cc.sifter.algorithms

import cc.sifter.Arm
import java.util.Random


/**
 * Created with IntelliJ IDEA.
 * User: alaiacano
 * Date: 9/29/13
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class BaseBandit(arms : Seq[Arm]) {

  protected val rand = new Random()
  protected def armCount = arms.size

  /**
   * Method to choose which arm will be returned to the user.
   */
  protected def chooseArm() : Arm

  /**
   * User facing method for selecting the next arm to use.
   */
  def selectArm() : Arm = {
    val arm = chooseArm()
    arm.incrementRequestCount()
    arm
  }

  def success(arm : Arm, value : Double) : Boolean = {
    arm.incrementPullCount
    recordSuccess(arm, value)
  }
  
  def failure(arm : Arm, value : Double) : Boolean = {
    arm.incrementPullCount
    recordFailure(arm, value)
  }

  protected def recordSuccess(arm : Arm, value : Double) : Boolean
  protected def recordFailure(arm : Arm, value : Double) : Boolean

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

}
