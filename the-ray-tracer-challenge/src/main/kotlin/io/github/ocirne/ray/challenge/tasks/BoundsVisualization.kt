package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Cube
import io.github.ocirne.ray.challenge.shapes.Group
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI

fun boundsVisualization(): Canvas {
    val c1 = Cube(material = Material(color=RED))
    val c2 = Cube(rotationZ(PI/4), Material(color=GREEN))
    val g = Group()
    g.addChild(c1)
    g.addChild(c2)

    val lightSource = PointLight(point(0, 4, -4), WHITE)
    val world = World(listOf(g), listOf(lightSource))

    val transform = viewTransform(point(-2.0, 2.0, -4.0), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 2, transform)
    // render the result to a canvas.
    return camera.render(world)
}
