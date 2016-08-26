package cc.sifter

import org.scalatest._
import java.util.Random

class EpsilonGreedySpec extends FlatSpec with Matchers {
  val rand = new Random(1)
  
  "An EpsilonGreedy algorithm" should "produce the right steady state output" in {
      
    val Npulls = 10000
    val epsilon = 0.2  // very safe value
    val test = EpsilonGreedy(Seq(Arm("one"), Arm("two"), Arm("three")), epsilon)
    
    for (i <- 1 to Npulls) {
      val selection: Selection = test.selectArm()
      val prob = selection.id match {
        case "one"   => .2
        case "two"   => .4
        case "three" => .8   // this should be the highest
      }
      val randVal = rand.nextDouble
      val updatedSelection = selection.copy(value = if (randVal < prob) 1.0 else 0.0)
      test.update(updatedSelection)
    }

    test.armsMap("three").pullCount should be > test.armsMap("one").pullCount
    test.armsMap("three").pullCount should be > test.armsMap("two").pullCount

    test.armsMap("three").value should be > test.armsMap("one").value
    test.armsMap("three").value should be > test.armsMap("two").value
    test.armsMap("two").value should be > test.armsMap("one").value
  }
}
