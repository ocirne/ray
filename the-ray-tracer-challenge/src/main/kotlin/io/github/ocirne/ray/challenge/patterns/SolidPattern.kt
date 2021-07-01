package io.github.ocirne.ray.challenge.patterns

import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point

class SolidPattern(val a: Color): Pattern() {

    override fun patternAt(objectPoint: Point): Color {
        return a
    }
}
