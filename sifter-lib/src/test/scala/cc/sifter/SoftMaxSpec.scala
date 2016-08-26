package cc.sifter

import org.scalatest._

import java.util.Random

class SoftMaxSpec extends FlatSpec with Matchers {
  val rand = new Random(1)
  
  "A SoftMax algorithm" should "produce the right steady state output" in {
      
    val Npulls = 10000
    val temperature = 0.5
    val test = SoftMax(Seq(Arm("one"), Arm("two"), Arm("three")), temperature)
    
    for (i <- 1 to Npulls) {
      val selection = test.selectArm()
      val prob = selection.id match {
        case "one"   => .2
        case "two"   => .4
        case "three" => .8   // this should be the highest
      }

      test.update(selection.copy(value = if (rand.nextDouble < prob) 1.0 else 0.0))
    }

    test.armsMap("three").pullCount should be > test.armsMap("one").pullCount
    test.armsMap("three").pullCount should be > test.armsMap("two").pullCount

    test.armsMap("three").value should be > test.armsMap("one").value
    test.armsMap("three").value should be > test.armsMap("two").value
    test.armsMap("two").value should be > test.armsMap("one").value
  }
}
