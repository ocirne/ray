package io.github.ocirne.ray.challenge.tuples

import io.github.ocirne.ray.challenge.math.equalsDelta
import kotlin.math.abs
import kotlin.random.Random

/** TODO Prüfen, ob mit Color zusammenführbar. Allerdings: Unterschiedliche Größe und Semantik. */
open class Color(val red: Double, val green: Double, val blue: Double) {

    constructor(red: Int, green: Int, blue: Int) : this(red.toDouble(), green.toDouble(), blue.toDouble())

    override fun toString(): String {
        return "color[$red $green $blue]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Color
        return red.equalsDelta(other.red) &&
               green.equalsDelta(other.green) &&
               blue.equalsDelta(other.blue)
    }

    operator fun plus(c: Color): Color {
        return Color(red + c.red, green + c.green, blue + c.blue)
    }

    operator fun minus(c: Color): Color {
        return Color(red - c.red, green - c.green, blue - c.blue)
    }

    operator fun unaryMinus(): Color {
        return Color(-red, -green, -blue)
    }

    /** Hadamard Product */
    operator fun times(c: Color): Color {
        return Color(red * c.red, green * c.green, blue * c.blue)
    }

    operator fun times(s: Double): Color {
        return Color(red * s, green * s, blue * s)
    }

    operator fun times(s: Int): Color {
        return this * s.toDouble()
    }

    operator fun div(s: Double): Color {
        return Color(red / s, green / s, blue / s)
    }

    operator fun div(s: Int): Color {
        return this / s.toDouble()
    }
}

fun color(red: Double, green: Double, blue: Double): Color {
    return Color(red, green, blue)
}

fun color(red: Int, green: Int, blue: Int): Color {
    return Color(red, green, blue)
}

fun color(): Color {
    return Color(Random.nextDouble(0.0, 1.0), Random.nextDouble(0.0, 1.0), Random.nextDouble(0.0, 1.0))
}

val BLACK = Color(0, 0, 0)
val WHITE = Color(1, 1, 1)
val RED = Color(1, 0, 0)
val GREEN = Color(0, 1, 0)
val DARK_GREEN = Color(0.0, 0.5, 0.0)
