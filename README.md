This is an implementation of a few multi-armed bandit algorithms in Scala. See [Sifter](http://www.sifter.cc) for more info.

## Setup

    git clone git@github.com:alaiacano/sifter-lib.git
    cd sifter-lib
    sbt assembly

## Example usage.

    import cc.sifter._
    import java.util.Random

    val rand = new Random(1)

    val Npulls = 10000

    val epsilon = 0.5
    val test = EpsilonGreedy(Seq(Arm("one"), Arm("two"), Arm("three")), epsilon)

    for (i <- 1 to Npulls) {
      val arm = test.selectArm()
      val prob = arm.getId match {
        case "one"   => .2
        case "two"   => .4
        case "three" => .8
      }

      if (rand.nextDouble < prob) {
          test.success(arm, 1.0)
      }
      else {
          test.failure(arm, 1.0)
      }
    }

    test.printInfo
