package io.github.ocirne.ray.weekend

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

fun random_cosine_direction(): vec3 {
    val r1 = Random.nextDouble()
    val r2 = Random.nextDouble()
    val z = sqrt(1-r2)
    val phi = 2*PI*r1
    val x = cos(phi)*sqrt(r2)
    val y = sin(phi)*sqrt(r2)
    return vec3(x, y, z)
}
