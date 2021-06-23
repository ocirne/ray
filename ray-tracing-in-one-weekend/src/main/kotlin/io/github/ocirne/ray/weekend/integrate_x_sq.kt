package io.github.ocirne.ray.weekend

import kotlin.random.Random

fun main() {
    val N = 1_000_000
    var sum = 0.0
    for (i in 0 until N) {
        val x = Random.nextDouble(0.0, 2.0)
        sum += x * x
    }
    println("I = ${2 * sum/N}")
}
