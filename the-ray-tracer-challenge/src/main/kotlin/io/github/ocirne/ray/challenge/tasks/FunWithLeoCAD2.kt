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

fun funWithLeoCAD2(): Canvas {

    val content = Canvas::class.java.classLoader.getResource("external/pc.obj")!!.readText()
    val materials = Canvas::class.java.classLoader.getResource("external/pc.mtl")!!.readText()
    val parser = ObjFileParser(content, materials)
    val g = parser.toGroup(translation(0.9, 150.6, -136.8).rotateX(1.5*PI).rotateY(1.2*PI))

    val lightSource = PointLight(point(150, 150, -300), WHITE)
    val world = World(listOf(g), listOf(lightSource))

    val transform = viewTransform(point(0, 100, -800), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(1680, 1050, PI / 2, transform)

    return camera.render(world)
}
