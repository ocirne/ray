package io.github.ocirne.ray.bewegt.math

import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random


class Vector3(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0) {

    constructor(x: Int = 0, y: Int = 0, z: Int = 0) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun unaryMinus(): Vector3 {
        return Vector3(-x, -y, -z)
    }

    operator fun times(v: Vector3): Vector3 {
        return Vector3(x * v.x, y * v.y, z * v.z)
    }

    operator fun plus(v: Vector3): Vector3 {
        return Vector3(x + v.x, y + v.y, z + v.z)
    }

    operator fun minus(v: Vector3): Vector3 {
        return Vector3(x - v.x, y - v.y, z - v.z)
    }

    operator fun times(t: Double): Vector3 {
        return Vector3(x * t, y * t, z * t)
    }

    operator fun div(t: Double): Vector3 {
        return this * (1 / t)
    }

    fun dot(v: Vector3): Double {
        return x * v.x + y * v.y + z * v.z
    }

    fun cross(v: Vector3): Vector3 {
        return Vector3(
            y * v.z - z * v.y,
            z * v.x - x * v.z,
            x * v.y - y * v.x
        )
    }

    fun unitVector(): Vector3 {
        return this / this.length()
    }

    fun length(): Double {
        return sqrt(lengthSquared())
    }

    fun lengthSquared(): Double {
        return x * x + y * y + z * z
    }

    fun nearZero(): Boolean {
        return abs(x) < epsilon && abs(y) < epsilon && abs(z) < epsilon
    }

    fun reflect(n: Vector3): Vector3 {
        return this - 2 * this.dot(n) * n
    }

    fun refract(n: Vector3, etai_over_etat: Double): Vector3 {
        val cosTheta = min((-this).dot(n), 1.0)
        val rOutPerp = etai_over_etat * (this + cosTheta * n)
        val rOutParallel = -sqrt(abs(1.0 - rOutPerp.lengthSquared())) * n
        return rOutPerp + rOutParallel
    }

    override fun toString(): String {
        return "$x $y $z"
    }

    companion object {

        val epsilon = 1e-8

        // Extension
        operator fun Double.times(v: Vector3): Vector3 =
            Vector3(this * v.x, this * v.y, this * v.z)

        fun random(): Vector3 {
            return Vector3(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
        }

        fun random(min: Double, max: Double): Vector3 {
            return Vector3(Random.nextDouble(min, max), Random.nextDouble(min, max), Random.nextDouble(min, max))
        }

        fun randomInUnitSphere(): Vector3 {
            while (true) {
                val p = random(-1.0, 1.0)
                if (p.lengthSquared() >= 1) {
                    continue
                }
                return p
            }
        }

        fun randomUnitVector(): Vector3 {
            return randomInUnitSphere().unitVector()
        }

        fun randomInUnitDisk(): Vector3 {
            while (true) {
                val p = Vector3(Random.nextDouble(-1.0, 1.0), Random.nextDouble(-1.0, 1.0), 0.0)
                if (p.lengthSquared() >= 1) {
                    continue
                }
                return p
            }
        }
    }
}

// Type aliases for Vector3
typealias Point3 = Vector3   // 3D point
typealias Color = Vector3    // RGB Color
