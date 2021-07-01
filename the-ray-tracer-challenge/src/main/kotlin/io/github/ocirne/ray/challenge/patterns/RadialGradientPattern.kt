package io.github.ocirne.ray.challenge.patterns

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point
import kotlin.math.floor
import kotlin.math.sqrt

class RadialGradientPattern(val a: Color,
                            val b: Color,
                            override val transform: Matrix = identityMatrix
): Pattern(transform) {

    override fun patternAt(objectPoint: Point): Color {
        val p = transform.inverse() * objectPoint
        val distance = b - a
        val radialDistance = sqrt(p.x * p.x + p.z * p.z)
        val fraction = radialDistance - floor(radialDistance)
        return a + distance * fraction
    }
}