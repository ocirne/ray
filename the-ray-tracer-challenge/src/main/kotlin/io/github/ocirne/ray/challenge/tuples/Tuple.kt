package io.github.ocirne.ray.challenge.tuples

import kotlin.math.abs

open class Tuple(val x: Double, val y: Double, val z: Double, val w: Double) {

    constructor(x: Int, y: Int, z: Int, w: Int): this(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())

    val epsilon = 0.00001

    private fun Double.equalsDelta(other: Double) = abs(this - other) < epsilon

    fun isPoint(): Boolean {
        return w.equalsDelta(1.0)
    }

    fun isVector(): Boolean {
        return w.equalsDelta(0.0)
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
}

fun point(x: Double, y: Double, z: Double): Tuple {
    return Tuple(x, y, z, 1.0)
}

fun point(x: Int, y: Int, z: Int) : Tuple {
    return Tuple(x, y, z, 1)
}

fun vector(x: Double, y: Double, z: Double): Tuple {
    return Tuple(x, y, z, 0.0)
}

fun vector(x: Int, y: Int, z: Int): Tuple {
    return Tuple(x, y, z, 0)
}
