package cc.sifter

case class Selection(val id: String, val value: Double = 0.0)

case class Arm(id: String, pullCount: Int = 0, requestCount: Int = 0, value: Double = 0.0) {

  def incrementPullCount(by: Int = 1): Arm = this.copy(pullCount = pullCount + by)

  def incrementRequestCount(by: Int = 1): Arm = this.copy(requestCount = requestCount + by)

}
