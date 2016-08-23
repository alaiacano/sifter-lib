package cc.sifter

import org.scalatest._

import java.util.Random

class AnnealingEpsilonGreedySpec extends FlatSpec with Matchers {
  val rand = new Random(1)
  
  "An AnnealingEpsilonGreedy algorithm" should "produce the right steady state output" in {
      
    val Npulls = 10000
    val test = AnnealingEpsilonGreedy(Seq(Arm("one"), Arm("two"), Arm("three")))
    
    for (i <- 1 to Npulls) {
      val selection = test.selectArm()
      val prob = selection.id match {
        case "one"   => .2
        case "two"   => .4
        case "three" => .8   // this should be the highest
      }


      test.update(selection.copy(value = if (rand.nextDouble < prob) 1.0 else 0.0))
    }

    test.arms(2).pullCount should be > (test.arms(0).pullCount)
    test.arms(2).pullCount should be > (test.arms(1).pullCount)

    test.arms(2).value should be > (test.arms(0).value)
    test.arms(2).value should be > (test.arms(1).value)
    test.arms(1).value should be > (test.arms(0).value)
  }
}
