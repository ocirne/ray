package io.github.ocirne.ray.weekend

import kotlin.math.PI

// Constants

const val infinity = 10.0*100

// Utility Extension

fun Double.degrees_to_radians() = this * PI / 180.0

fun Double.clamp(min: Double= 0.0, max: Double= 0.999): Double =
    if (this < min) min else if (this > max) max else this

fun Double.clamp_and_stretch(min: Double= 0.0, max: Double= 0.999): Int =
    (256 * this.clamp(min, max)).toInt()

fun Int.r(): Int = (this shr 16) and 0xFF

fun Int.g(): Int = (this shr 8) and 0xFF

fun Int.b(): Int = this and 0xFF
