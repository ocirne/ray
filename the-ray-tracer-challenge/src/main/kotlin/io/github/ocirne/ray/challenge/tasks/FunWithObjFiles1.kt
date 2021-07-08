package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.parser.ObjFileParser
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.transformations.viewTransform
import io.github.ocirne.ray.challenge.tuples.WHITE
import io.github.ocirne.ray.challenge.tuples.point
import io.github.ocirne.ray.challenge.tuples.vector
import kotlin.math.PI

val f1 = "cow-nonormals.obj"
val f2 = "plane.obj"
val f3 = "pumpkin_tall_10k.obj"
val f4 = "teapot.obj"
val f5 = "teapot-low.obj"
val f6 = "teddy.obj"
val f7 = "utah-teapot.obj"

fun funWithObjFiles1(): Canvas {

    val content = Canvas::class.java.classLoader.getResource("external/$f2")!!.readText()
    val parser = ObjFileParser(content)
    val g = parser.toGroup()

    val lightSource = PointLight(point(-10, 10, -10), WHITE)
    val world = World(listOf(g), listOf(lightSource))

    val transform = viewTransform(point(0, 0, -10), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 2, transform)

    return camera.render(world)
}
