package io.github.ocirne.ray.challenge.tuples

import kotlin.math.abs
import kotlin.math.sqrt

open class Tuple(val x: Double, val y: Double, val z: Double, val w: Double) {

    constructor(x: Int, y: Int, z: Int, w: Int) : this(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())

    val epsilon = 0.00001

    private fun Double.equalsDelta(other: Double) = abs(this - other) < epsilon

    fun isPoint(): Boolean {
        return w.equalsDelta(1.0)
    }

    fun isVector(): Boolean {
        return w.equalsDelta(0.0)
    }

    override fun toString(): String {
        return "tuple[$x $y $z $w]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Tuple
        return x.equalsDelta(other.x) &&
                y.equalsDelta(other.y) &&
                z.equalsDelta(other.z) &&
                w.equalsDelta(other.w)
    }

    operator fun plus(t: Tuple): Tuple {
        return Tuple(x + t.x, y + t.y, z + t.z, w + t.w)
    }

    operator fun minus(t: Tuple): Tuple {
        return Tuple(x - t.x, y - t.y, z - t.z, w - t.w)
    }

    operator fun unaryMinus(): Tuple {
        return Tuple(-x, -y, -z, -w)
    }

    operator fun times(s: Double): Tuple {
        return Tuple(x * s, y * s, z * s, w * s)
    }

    operator fun times(s: Int): Tuple {
        return this * s.toDouble()
    }

    operator fun div(s: Double): Tuple {
        return Tuple(x / s, y / s, z / s, w / s)
    }

    operator fun div(s: Int): Tuple {
        return this / s.toDouble()
    }

    fun magnitude(): Double {
        return sqrt(x * x + y * y + z * z + w * w)
    }

    fun normalize(): Tuple {
        return Tuple(x, y, z, w) / magnitude()
    }

    fun dot(b: Tuple): Double {
        return x * b.x + y * b.y + z * b.z + w * b.w
    }

    fun cross(b: Tuple): Tuple {
        return vector(
            y * b.z - z * b.y,
            z * b.x - x * b.z,
            x * b.y - y * b.x
        )
    }
}

fun point(x: Double, y: Double, z: Double): Tuple {
    return Tuple(x, y, z, 1.0)
}

fun point(x: Int, y: Int, z: Int): Tuple {
    return Tuple(x, y, z, 1)
}

fun vector(x: Double, y: Double, z: Double): Tuple {
    return Tuple(x, y, z, 0.0)
}

fun vector(x: Int, y: Int, z: Int): Tuple {
    return Tuple(x, y, z, 0)
}

typealias Point = Tuple
typealias Vector = Tuple
