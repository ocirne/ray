package io.github.ocirne.ray.challenge.patterns

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point

abstract class Pattern(open val transform: Matrix = identityMatrix) {

    fun patternAtShape(shape: Shape, worldPoint: Point): Color {
        val objectPoint = shape.transform.inverse() * worldPoint
        return patternAt(objectPoint)
    }

    abstract fun patternAt(objectPoint: Point): Color
}
