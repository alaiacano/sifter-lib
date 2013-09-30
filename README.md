This is an implementation of a few multi-armed bandit algorithms in Scala. See [Sifter](http://www.sifter.cc) for more info.

I am very open to pull requests with new algorithms (I'm working on more) and especially idiomatic code changes (I'm still learning scala).

## Setup

    git clone git@github.com:alaiacano/sifter-lib.git
    cd sifter-lib
    sbt assembly

## Example usage.

```scala
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

// Arms:   one, two, three
// Pulls:  1726, 1676, 6598
// Values: -1028.0, -310.0, 3890.0
```
