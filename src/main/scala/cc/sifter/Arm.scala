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

class Arm(id: String, pullCount: Int, requestCount: Int, value: Double) {

  private val _id = id
  private var _pullCount = pullCount
  private var _requestCount = requestCount
  private var _value = value

  def incrementPullCount() { _pullCount += 1 }
  def decrementPullCount() { _pullCount -= 1 }

  def incrementRequestCount() { _requestCount += 1 }
  def decrementRequestCount() { _requestCount -= 1 }

  def value_=(v: Double)        { _value = v }
  def incrementValue(v: Double) { value = value + v }

  def pullCount = _pullCount
  def value = _value
  def id = _id
  def requestCount = _requestCount

  override def toString() = {
    Seq[String](id, pullCount.toString, requestCount.toString, value.toString).mkString(",")
  }
}
