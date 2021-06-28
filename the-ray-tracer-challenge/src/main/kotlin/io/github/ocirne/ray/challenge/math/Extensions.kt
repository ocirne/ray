package io.github.ocirne.ray.challenge.math

import kotlin.math.abs

const val epsilon = 0.00001

fun Double.equalsDelta(other: Double) = abs(this - other) < epsilon
