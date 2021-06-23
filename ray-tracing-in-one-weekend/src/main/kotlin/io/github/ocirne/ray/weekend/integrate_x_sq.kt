package io.github.ocirne.ray.weekend

import kotlin.math.pow
import kotlin.random.Random

fun pdf(x: Double): Double {
    return 3.0*x*x/8.0
}

fun main() {
    val N = 1
    var sum = 0.0
    for (i in 0 until N) {
        val x = Random.nextDouble(0.0, 8.0).pow(1.0/3.0)
        sum += x * x / pdf(x)
    }
    println("I = ${sum/N}")
}
