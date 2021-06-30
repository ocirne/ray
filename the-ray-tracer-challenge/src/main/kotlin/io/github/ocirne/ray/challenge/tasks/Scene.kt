package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.raysphere.Sphere
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import java.io.File
import kotlin.math.PI
import kotlin.random.Random

fun scene(): Canvas {
    val floorMaterial = Material(
        color = color(1.0, 0.9, 0.9),
        specular = 0.0
    )

    val floor = Sphere(
        transform = scaling(10.0, 0.01, 10.0),
        material = floorMaterial
    )

    val leftWall = Sphere(
        transform = translation(0, 0, 5) * rotationY(-PI / 4) * rotationX(PI / 2) * scaling(10.0, 0.01, 10.0),
        material = floor.material
    )

    val rightWall = Sphere(
        transform = translation(0, 0, 5) * rotationY(PI / 4) * rotationX(PI / 2) * scaling(10.0, 0.01, 10.0),
        material = floor.material
    )

    val middle = Sphere(
        transform = translation(-0.5, 1.0, 0.5),
        Material(
            color = color(0.1, 1.0, 0.5),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val right = Sphere(
        transform = translation(1.5, 0.5, -0.5) * scaling(0.5, 0.5, 0.5),
        material = Material(
            color = color(0.5, 1.0, 0.1),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val left = Sphere(
        transform = translation(-1.5, 0.33, -0.75) * scaling(0.33, 0.33, 0.33),
        material = Material(
            color = color(1.0, 0.8, 0.1),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val additionalSpheres = (0..10).map {
    val tx = Random.nextDouble(-2.0, 2.0)
    val ty = Random.nextDouble(1.0, 3.0)
    val tz = Random.nextDouble(-2.0, 2.0)
    val scale = Random.nextDouble(0.2, 1.0)
    Sphere(
        transform = translation(tx, ty, tz) * scaling(scale, scale, scale),
        material = Material(
            color = color(),
            diffuse = Random.nextDouble(0.0, 1.0),
            specular = Random.nextDouble(0.0, 1.0)
        )
    )
    }

    val lightSource = PointLight(point(-10, 10, -10), WHITE)
    val world = World(listOf(floor, leftWall, rightWall, middle, right, left) + additionalSpheres, listOf(lightSource))

    val transform = viewTransform(point(0.0, 1.5, -5.0), point(0, 1, 0), vector(0, 1, 0))
    val camera = Camera(200, 200, PI / 2, transform)
    // render the result to a canvas.
    return camera.render(world)
}

fun main() {
    val canvas = scene()
    File("output/scene.ppm").printWriter().use(canvas::toPPM)
}
