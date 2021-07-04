package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Group
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI
import kotlin.random.Random

fun boundsVisualization2Opt(): Canvas {
    val floorMaterial = Material(color = ELFENBEIN, specular = 0.0)
    val marbleMaterial = Material(color = DARK_GRAY, reflective = 1.0)

    val floor = Plane(translation(0, 0, 0), material = floorMaterial)
    val g1 = Group()
    val g2 = Group()
    val g3 = Group()
    val g4 = Group()
    for (i in 0..300) {
        val posX = Random.nextDouble() * 20 - 10
        val size = Random.nextDouble()
        val posZ = Random.nextDouble() * 20 - 10
        val s = Sphere(translation(posX, size, posZ), material = marbleMaterial)
        when {
            (posX < 0 && posZ < 0) -> g1.addChild(s)
            (posX < 0 && posZ >= 0) -> g2.addChild(s)
            (posX >= 0 && posZ < 0) -> g3.addChild(s)
            (posX >= 0 && posZ >= 0) -> g4.addChild(s)
            else -> throw IllegalStateException()
        }
    }

    val lightSource = PointLight(point(0, 4, -4), WHITE)
    val world = World(listOf(floor, g1, g2, g3, g4), listOf(lightSource))

    val transform = viewTransform(point(0.0, 8.0, -8.0), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 2, transform)
    // render the result to a canvas.
    return camera.render(world)
}
