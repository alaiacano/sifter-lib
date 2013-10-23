package cc.sifter

/**
 * Created with IntelliJ IDEA.
 * User: alaiacano
 * Date: 9/29/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */


case class Selection(val id: String, var value: Double = 0.0)

object Arm {
  private val serializationDelim = ","

  def apply(id: String) = new Arm(id, 0, 0, 0.0)

  def apply(id: String, pullCount: Int, requestCount: Int, value: Double) = new Arm(id.replace(serializationDelim, ""), pullCount, requestCount, value)

  def checkIdFormat(id: String): Boolean = !id.contains(serializationDelim)
}

class Arm(private val id: String, private var pullCount: Int, private var requestCount: Int, private var value: Double) {

  def incrementPullCount() { pullCount += 1 }
  def decrementPullCount() { pullCount -= 1 }

  def incrementRequestCount() { requestCount += 1 }
  def decrementRequestCount() { requestCount -= 1 }

  def setValue(v: Double)       { value = v }
  def incrementValue(v: Double) { value += v }

  def getPullCount = pullCount
  def getValue = value
  def getId = id
  def getRequestCount = requestCount

  override def toString() = {
    Seq[String](id, pullCount.toString, requestCount.toString, value.toString).mkString(",")
  }
}
