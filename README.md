[![Build Status](https://travis-ci.org/alaiacano/sifter-lib.svg?branch=master)](https://travis-ci.org/alaiacano/sifter-lib)

# Sifter

This is an implementation of a few multi-armed bandit algorithms in Scala.

The API is not yet in a stable state, but here's a working example of a simulation. We'll run 1000 trials using an `EpsilonGreedy`
bandit and see which selections get used more. We should see that option `three` is used the most since we've given it the highest
chance of success.

```scala
// this can be run in `sbt sifterLib/console`
import cc.sifter._
import java.util.Random

val nPulls = 10000
val epsilon = 0.2  // very safe value
val mabTest = EpsilonGreedy(Seq(Arm("one"), Arm("two"), Arm("three")), epsilon)
val rand = new Random(1)

// We'll simulate the success rate of each of the Arms. If you really knew these values,
// you wouldn't need to test anything!
val chanceOfSuccess = Map(
  "one"   -> .2,
  "two"   -> .4,
  "three" -> .8
)

for (i <- 1 to nPulls) {
  val selection: Selection = mabTest.selectArm()

  // Simulate the result of the trial! In reality this is where you show a webpage or make a prediction
  // based on the `Selection` that you were just given.
  //
  // The trial was either a success (with simulated probability chanceOfSuccess(selection.id)) and value 1.0
  // or a failure with value 0.0
  val simulatedResult: Double = if (rand.nextDouble < chanceOfSuccess(selection.id)) 1.0 else 0.0

  // after the trial, update the state with the results.
  val updatedSelection: Selection = selection.copy(value = simulatedResult)
  mabTest.update(updatedSelection)
}
```

As a reminder, `EpsilonGreedy`'s rule is:

* With probability `epsilon`, choose the arm that is performing the best
* Otherwise choose randomly

```scala
mabTest
  .armsMap
  .values
  .toList
  .sortBy(_.pullCount)(Ordering[Int].reverse)

/////

List(
  // These are Arm(id, pullCount, requestCount, totalValue)
  Arm(three,4721,4721,3754.0),
  Arm(one,2647,2647,530.0),
  Arm(two,2632,2632,1038.0)
)
```