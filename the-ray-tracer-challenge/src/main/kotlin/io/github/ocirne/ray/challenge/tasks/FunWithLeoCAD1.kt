package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.parser.ObjFileParser
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.transformations.viewTransform
import io.github.ocirne.ray.challenge.tuples.WHITE
import io.github.ocirne.ray.challenge.tuples.point
import io.github.ocirne.ray.challenge.tuples.vector
import kotlin.math.PI

fun funWithLeoCAD1(): Canvas {

    val objects = Canvas::class.java.classLoader.getResource("external/laterne.obj")!!.readText()
    val materials = Canvas::class.java.classLoader.getResource("external/laterne.mtl")!!.readText()
    val parser = ObjFileParser(objects, materials)
    val g = parser.toGroup(translation(-10, 10, -14).rotateX(1.5*PI).rotateY(1.2*PI))
    println((g.bounds().maximum + g.bounds().minimum) / 2)
    val lightSource = PointLight(point(-30, 30, -30), WHITE)
    val world = World(listOf(g), listOf(lightSource))

    val transform = viewTransform(point(0, 10, -30), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(200, 200, PI / 2, transform)

    return camera.render(world)
}
