package io.github.ocirne.ray.challenge.patterns

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point
import kotlin.math.floor

class StripePattern(val a: Color, val b: Color, val transform: Matrix = identityMatrix) {

    fun stripeAt(p: Point): Color {
        if (floor(p.x).toInt() % 2 == 0) {
            return a
        }
        return b
    }

    fun stripeAtShape(shape: Shape, worldPoint: Point): Color {
        val objectPoint = shape.transform.inverse() * worldPoint
        val patternPoint = transform.inverse() * objectPoint
        return stripeAt(patternPoint)
    }
}
