package io.github.ocirne.ray.weekend

import kotlin.random.Random

fun main() {
    var inside_circle = 0
    var runs = 0
    while (true) {
        runs++
        val x = Random.nextDouble(-1.0,1.0)
        val y = Random.nextDouble(-1.0,1.0)
        if (x*x + y*y < 1) {
            inside_circle++
        }
        if(runs % 100_000 == 0) {
            println("Estimate of Pi = ${4.0 * inside_circle / runs}")
        }
    }
}
