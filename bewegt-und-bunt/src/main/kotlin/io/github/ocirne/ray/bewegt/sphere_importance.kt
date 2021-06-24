package io.github.ocirne.ray.bewegt

import kotlin.math.PI

fun pdf(x: vec3): Double {
    return 1 / (4*PI)
}

fun main() {
    val N = 1_000_000
    var sum = 0.0
    for (i in 0 until N) {
        val d = vec3.random_unit_vector()
        val cosine_squared = d.z()*d.z()
        sum += cosine_squared / pdf(d)
    }
    println("I = ${sum/N}")
}
