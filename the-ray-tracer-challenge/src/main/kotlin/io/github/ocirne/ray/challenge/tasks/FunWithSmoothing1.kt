package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.parser.ObjFileParser
import io.github.ocirne.ray.challenge.patterns.CheckersPattern
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.transformations.rotationX
import io.github.ocirne.ray.challenge.transformations.viewTransform
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI

fun funWithSmoothing1(): Canvas {

    val floorMaterial = Material(specular = 0.0, pattern = CheckersPattern(DARK_GRAY, WHITE))
    val floor = Plane(material = floorMaterial)

    val content = Canvas::class.java.classLoader.getResource("external/teapot-low.obj")!!.readText()
    val parser = ObjFileParser(content)
    val g = parser.toGroup(rotationX(-PI/2))

    val lightSource = PointLight(point(-20, 20, -20), WHITE)
    val world = World(listOf(floor, g), listOf(lightSource))

    val transform = viewTransform(point(0, 20, -20), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 3, transform)

    return camera.render(world)
}
