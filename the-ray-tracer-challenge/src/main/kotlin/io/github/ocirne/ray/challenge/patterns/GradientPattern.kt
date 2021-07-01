package io.github.ocirne.ray.challenge.patterns

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point
import kotlin.math.floor

class GradientPattern(
    val a: Color,
    val b: Color,
    override val transform: Matrix = identityMatrix
) : Pattern(transform) {

    override fun patternAt(point: Point): Color {
        val distance = b - a
        val fraction = point.x - floor(point.x)
        return a + distance * fraction
    }
}
