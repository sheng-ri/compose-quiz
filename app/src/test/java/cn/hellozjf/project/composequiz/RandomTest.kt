package cn.hellozjf.project.composequiz

import org.junit.Test
import kotlin.random.Random

class RandomTest {
  @Test
  fun test() {
    val random1 = Random(20251121L)
    println("random1")
    for (i in 0 until 20) {
      println(random1.nextInt(1065))
    }
    println()
    println()
    println()

    val random2 = Random(20251121L)
    println("random2")
    for (i in 0 until 20) {
      println(random2.nextInt(1065))
    }
  }
}