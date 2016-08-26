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

    test.arm("three").map(_.pullCount) should be > test.arm("one").map(_.pullCount)
    test.arm("three").map(_.pullCount) should be > test.arm("two").map(_.pullCount)

    test.arm("three").map(_.value) should be > test.arm("one").map(_.value)
    test.arm("three").map(_.value) should be > test.arm("two").map(_.value)
    test.arm("two").map(_.value) should be > test.arm("one").map(_.value)
  }
}
