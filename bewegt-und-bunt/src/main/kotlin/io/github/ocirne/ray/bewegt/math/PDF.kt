package io.github.ocirne.ray.bewegt.math

import io.github.ocirne.ray.bewegt.hittable.Hittable
import io.github.ocirne.ray.bewegt.math.Vector3.Companion.randomUnitVector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

interface PDF {

    fun value(direction: Vector3): Double

    fun generate(): Vector3
}

fun randomCosineDirection(): Vector3 {
    val r1 = Random.nextDouble()
    val r2 = Random.nextDouble()
    val z = sqrt(1 - r2)
    val phi = 2 * PI * r1
    val x = cos(phi) * sqrt(r2)
    val y = sin(phi) * sqrt(r2)
    return Vector3(x, y, z)
}

class CosinePDF(w: Vector3) : PDF {

    private val uvw = ONB.buildFromW(w)

    override fun value(direction: Vector3): Double {
        val cosine = direction.unitVector().dot(uvw.w)
        return if (cosine <= 0) 0.0 else cosine / PI
    }

    override fun generate(): Vector3 {
        return uvw.local(randomCosineDirection())
    }
}

class HittablePDF(private val ptr: Hittable, private val origin: Point3) : PDF {

    override fun value(direction: Vector3): Double {
        return ptr.pdfValue(origin, direction)
    }

    override fun generate(): Vector3 {
        return ptr.random(origin)
    }
}

class MixturePDF(private val p0: PDF, private val p1: PDF) : PDF {

    override fun value(direction: Vector3): Double {
        return 0.5 * p0.value(direction) + 0.5 * p1.value(direction)
    }

    override fun generate(): Vector3 {
        return if (Random.nextDouble() < 0.5) p0.generate() else p1.generate()
    }
}

class SpherePDF : PDF {

    override fun value(direction: Vector3): Double {
        return 1/ (4 * PI)
    }

    override fun generate() : Vector3 {
        return randomUnitVector()
    }
}

fun randomToSphere(radius: Double, distance_squared: Double): Vector3 {
    val r1 = Random.nextDouble()
    val r2 = Random.nextDouble()
    val z = 1 + r2 * (sqrt(1 - radius * radius / distance_squared) - 1)

    val phi = 2 * PI * r1
    val x = cos(phi) * sqrt(1 - z * z)
    val y = sin(phi) * sqrt(1 - z * z)

    return Vector3(x, y, z)
}
