This is an implementation of a few multi-armed bandit algorithms in Scala. See [Sifter](http://www.sifter.cc) for more info.

I am very open to pull requests with new algorithms (I'm working on more) and especially idiomatic code changes (I'm still learning scala).

## Setup

    git clone git@github.com:alaiacano/sifter-lib.git
    cd sifter-lib
    sbt assembly

You can also fork this repo and use [this method](http://stackoverflow.com/a/8828803) to pull the 
code directly into your sbt project (I wouldn't recommend using my repo as a dependency
as I'm still activiely committing to it and the API isn't guaranteed to be stable yet).

## Example usage.

Here's a simulation that's run 1000 times. There are three different options ("arms"):
`one`, `two`, and `three`, and we run 1000 experiments ("pulls"). Our simulation will
return a value of 1.0 with a probability of 0.2, 0.4, and 0.8 for the respective arms.

```scala
import cc.sifter._
import java.util.Random

val rand = new Random(1)

val Npulls = 1000

// initialize an EpsilonGreedy test
val epsilon = 0.5
val test = EpsilonGreedy(Seq(Arm("one"), Arm("two"), Arm("three")), epsilon)

val conversionProbabilities = Map("one" -> .2, "two" -> .4, "three" -> .8)

for (i <- 1 to Npulls) {

  // select an arm for this experiment
  val selection: Selection = test.selectArm()

  // simulate the test and assign a value.
  val successfulConversion = rand.nextDouble < conversionProbabilities(selection.id)
  selection.value = if (successfulConversion) 1.0 else 0.0

  // update the test with the result.
  test.update(selection)
}

test.printInfo

// Arms:   one, two, three
// Pulls:  178, 170, 652
// Values: 44.0, 70.0, 513.0
```
