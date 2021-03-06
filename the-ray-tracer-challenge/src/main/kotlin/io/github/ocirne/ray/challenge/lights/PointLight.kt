package io.github.ocirne.ray.challenge.lights

import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point

data class PointLight(val position: Point, val intensity: Color)
