package io.github.ocirne.ray.weekend

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

interface pdf {

    fun value(direction: vec3): Double
    fun generate(): vec3
}

fun random_cosine_direction(): vec3 {
    val r1 = Random.nextDouble()
    val r2 = Random.nextDouble()
    val z = sqrt(1-r2)
    val phi = 2*PI*r1
    val x = cos(phi)*sqrt(r2)
    val y = sin(phi)*sqrt(r2)
    return vec3(x, y, z)
}

class cosine_pdf(w: vec3): pdf {

    val uvw = onb.build_from_w(w)

    override fun value(direction: vec3): Double {
        val cosine = direction.unitVector().dot(uvw.w)
        return if (cosine <= 0) 0.0 else cosine/PI
    }

    override fun generate(): vec3 {
        return uvw.local(random_cosine_direction())
    }
}
