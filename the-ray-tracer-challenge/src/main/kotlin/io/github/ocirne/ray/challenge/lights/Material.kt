package io.github.ocirne.ray.challenge.lights

import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.WHITE

data class Material(
    val color: Color = WHITE,
    val ambient: Double = 0.1,
    val diffuse: Double = 0.9,
    val specular: Double = 0.9,
    val shininess: Double = 200.0
)
