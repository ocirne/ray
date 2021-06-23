package io.github.ocirne.ray.weekend

import kotlin.random.Random

fun main() {
    var inside_circle = 0
    var inside_circle_stratified = 0
    val sqrt_N = 10000

    for (i in 0 until sqrt_N) {
        for (j in 0 until sqrt_N) {
            val x = Random.nextDouble(-1.0, 1.0)
            val y = Random.nextDouble(-1.0, 1.0)
            if (x * x + y * y < 1) {
                inside_circle++
            }
            val xs = 2 * ((i + Random.nextDouble()) / sqrt_N) - 1
            val ys = 2 * ((j + Random.nextDouble()) / sqrt_N) - 1
            if (xs * xs + ys * ys < 1)
                inside_circle_stratified++
        }
    }
    val N = sqrt_N.toDouble() * sqrt_N
    println("Regular Estimate of Pi = ${4.0 * inside_circle / N}")
    println("Stratified Estimate of Pi = ${4.0 * inside_circle_stratified / N}")
}
