package cc.sifter

/**
 * Created with IntelliJ IDEA.
 * User: alaiacano
 * Date: 9/29/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */

object Arm {
  def apply(id : String) = new Arm(id, 0, 0, 0.0)

  def apply(id : String, pullCount : Int, requestCount : Int, value : Double) = new Arm(id, pullCount, requestCount, value)
}

class Arm(private val id : String, private var pullCount : Int, private var requestCount : Int, private var value : Double) {

  def incrementPullCount() { pullCount += 1 }
  def decrementPullCount() { pullCount -= 1 }

  def incrementRequestCount() { requestCount += 1 }
  def decrementRequestCount() { requestCount -= 1 }

  def setValue(v : Double)       { value = v }
  def incrementValue(v : Double) { value += math.abs(v) }
  def decrementValue(v : Double) { value -= math.abs(v) }

  def getPullCount = pullCount
  def getValue = value
  def getId = id
  def getRequestCount = requestCount
}
