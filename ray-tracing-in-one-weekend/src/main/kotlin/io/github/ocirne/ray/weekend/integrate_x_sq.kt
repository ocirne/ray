package io.github.ocirne.ray.weekend

import kotlin.math.sqrt
import kotlin.random.Random

fun pdf(x: Double): Double {
    return 0.5*x
}

fun main() {
    val N = 1_000_000
    var sum = 0.0
    for (i in 0 until N) {
        val x = sqrt(Random.nextDouble(0.0, 4.0))
        sum += x * x / pdf(x)
    }
    println("I = ${sum/N}")
}
