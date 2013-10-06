package cc.sifter

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._

import java.util.Random

class SoftMaxSpec extends FlatSpec {
  val rand = new Random(1)
  
  "A SoftMax algorithm" should "produce the right steady state output" in {
      
    val Npulls = 10000
    val temperature = 0.5
    val test = SoftMax(Seq(Arm("one"), Arm("two"), Arm("three")), temperature)
    
    for (i <- 1 to Npulls) {
      val arm = test.selectArm()
      val prob = arm.getId match {
        case "one"   => .2
        case "two"   => .4
        case "three" => .8
      }

      if (rand.nextDouble < prob) {
          test.update(arm, 1.0)
      }
      else {
          test.update(arm, -1.0)
      }
    }

    test.arms(2).getPullCount should be > (test.arms(0).getPullCount)
    test.arms(2).getPullCount should be > (test.arms(1).getPullCount)

    test.arms(2).getValue should be > (test.arms(0).getValue)
    test.arms(2).getValue should be > (test.arms(1).getValue)
    test.arms(1).getValue should be > (test.arms(0).getValue)
  }
}
