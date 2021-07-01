package io.github.ocirne.ray.challenge.patterns

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point

class BlendedPattern(val a: Pattern,
                     val b: Pattern,
                     override val transform: Matrix = identityMatrix
): Pattern(transform) {

    override fun patternAt(objectPoint: Point): Color {
        val p = transform.inverse() * objectPoint
        val colorA = a.patternAt(p)
        val colorB = b.patternAt(p)
        return (colorA + colorB) / 2
    }
}
