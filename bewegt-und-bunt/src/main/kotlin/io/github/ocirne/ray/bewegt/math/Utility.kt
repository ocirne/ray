package io.github.ocirne.ray.bewegt.math

import kotlin.math.PI

// Constants

const val infinity = 10.0*100

// Utility Extension

fun Double.degreesToRadians() = this * PI / 180.0

fun Double.clamp(min: Double= 0.0, max: Double= 0.999): Double =
    if (this < min) min else if (this > max) max else this

fun Double.clampAndStretch(min: Double= 0.0, max: Double= 0.999): Int =
    (256 * this.clamp(min, max)).toInt()
