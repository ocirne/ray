package io.github.ocirne.ray.challenge.patterns

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point
import kotlin.math.floor

class StripePattern(val a: Color,
                    val b: Color,
                    override val transform: Matrix = identityMatrix): Pattern(transform) {

    override fun patternAt(objectPoint: Point): Color {
        val p = transform.inverse() * objectPoint
        return if (floor(p.x).toInt() % 2 == 0) a else b
    }
}
