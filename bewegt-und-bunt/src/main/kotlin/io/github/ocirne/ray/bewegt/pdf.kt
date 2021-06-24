package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

interface pdf {

    fun value(direction: Vector3): Double
    fun generate(): Vector3
}

fun random_cosine_direction(): Vector3 {
    val r1 = Random.nextDouble()
    val r2 = Random.nextDouble()
    val z = sqrt(1-r2)
    val phi = 2*PI*r1
    val x = cos(phi)*sqrt(r2)
    val y = sin(phi)*sqrt(r2)
    return Vector3(x, y, z)
}

class cosine_pdf(w: Vector3): pdf {

    val uvw = onb.build_from_w(w)

    override fun value(direction: Vector3): Double {
        val cosine = direction.unitVector().dot(uvw.w)
        return if (cosine <= 0) 0.0 else cosine/PI
    }

    override fun generate(): Vector3 {
        return uvw.local(random_cosine_direction())
    }
}

class hittable_pdf(val ptr: hittable, val origin: Point3): pdf {

    override fun value(direction: Vector3): Double {
        return ptr.pdf_value(origin, direction)
    }

    override fun generate(): Vector3 {
        return ptr.random(origin)
    }
}

class mixture_pdf(val p0: pdf, val p1: pdf): pdf {

    override fun value(direction: Vector3): Double {
        return 0.5 * p0.value(direction) + 0.5 *p1.value(direction)
    }

    override fun generate(): Vector3 {
        return if (Random.nextDouble() < 0.5) p0.generate() else p1.generate()
    }
}

fun random_to_sphere(radius: Double, distance_squared: Double): Vector3 {
    val r1 = Random.nextDouble()
    val r2 = Random.nextDouble()
    val z = 1 + r2*(sqrt(1-radius*radius/distance_squared) - 1)

    val phi = 2*PI*r1
    val x = cos(phi)*sqrt(1-z*z)
    val y = sin(phi)*sqrt(1-z*z)

    return Vector3(x, y, z)
}
