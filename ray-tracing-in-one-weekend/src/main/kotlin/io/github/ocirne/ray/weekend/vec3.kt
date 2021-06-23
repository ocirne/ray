package io.github.ocirne.ray.weekend

import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random


class vec3(val x: Double=0.0, val y: Double=0.0, val z: Double=0.0) {

    constructor(x: Int=0, y: Int=0, z: Int=0): this(x.toDouble(), y.toDouble(), z.toDouble())

    fun x(): Double { return x }
    fun y(): Double { return y }
    fun z(): Double { return z }

    operator fun unaryMinus(): vec3 {
        return vec3(-x, -y, -z)
    }

    operator fun times(v: vec3): vec3 {
        return vec3(x * v.x, y * v.y, z * v.z)
    }

    operator fun plus(v: vec3): vec3 {
        return vec3(x + v.x, y + v.y, z + v.z)
    }

    operator fun minus(v: vec3): vec3 {
        return vec3(x - v.x, y - v.y, z - v.z)
    }

    operator fun times(t: Double): vec3 {
        return vec3(x*t, y*t, z*t)
    }

    operator fun div(t: Double): vec3 {
        return this * (1/t)
    }

    fun dot(v: vec3): Double {
        return x * v.x + y * v.y + z * v.z
    }

    fun cross(v: vec3): vec3 {
        return vec3(y * v.z - z * v.y,
                    z * v.x - x * v.z,
                    x * v.y - y * v.x)
    }

    fun unitVector(): vec3 {
        return this / this.length()
    }

    fun length(): Double {
        return sqrt(length_squared())
    }

    fun length_squared(): Double {
        return x*x + y*y + z*z
    }

    fun near_zero(): Boolean {
        val eps = 1e-8
        return abs(x) < eps && abs(y) < eps && abs(z) < eps
    }

    fun reflect(n: vec3): vec3 {
        return this - 2*this.dot(n)*n
    }

    fun refract(n: vec3, etai_over_etat: Double): vec3 {
        val cos_theta = min((-this).dot(n), 1.0)
        val r_out_perp =  etai_over_etat * (this + cos_theta*n)
        val r_out_parallel = -sqrt(abs(1.0 - r_out_perp.length_squared())) * n
        return r_out_perp + r_out_parallel
    }

    override fun toString(): String {
        return "${x} ${y} ${z}"
    }

    companion object {
        fun random(): vec3 {
            return vec3(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
        }

        fun random(min: Double, max: Double): vec3 {
            return vec3(Random.nextDouble(min, max), Random.nextDouble(min, max), Random.nextDouble(min, max))
        }

        fun random_in_unit_sphere(): vec3 {
            while (true) {
                val p = random(-1.0, 1.0)
                if (p.length_squared() >= 1) {
                    continue
                }
                return p
            }
        }

        fun random_unit_vector(): vec3 {
            return random_in_unit_sphere().unitVector()
        }

        fun random_in_unit_disk(): vec3 {
            while (true) {
                val p = vec3(Random.nextDouble(-1.0, 1.0), Random.nextDouble(-1.0, 1.0), 0.0)
                if (p.length_squared() >= 1) {
                    continue
                }
                return p
            }
        }
    }
}

// Type aliases for Vec3
typealias point3 = vec3   // 3D point
typealias color = vec3    // RGB color

operator fun Double.times(v: vec3): vec3 =
    vec3(this * v.x, this * v.y, this * v.z)
