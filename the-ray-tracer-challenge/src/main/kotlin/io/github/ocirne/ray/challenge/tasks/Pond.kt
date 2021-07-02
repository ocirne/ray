package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.patterns.CheckersPattern
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI

fun pond(): Canvas {
    val floorMaterial = Material(pattern=CheckersPattern(BLACK, WHITE), specular = 0.0)
    val surfaceMaterial = Material(color = BLUE, specular = 0.0, transparency = 1.0)

    val pondFloor = Plane(transform = translation(0, -5, 0), material = floorMaterial)
    val pondSurface = Plane(transform = translation(0, -2, 0), material = surfaceMaterial)

    val right = Sphere(
        transform = translation(3.0, -4.0, 1.5) * scaling(1.0, 0.5, 1.0),
        material = Material(
            color = color(0.3, 0.3, 0.3),
            diffuse = 0.7,
            specular = 0.0
        )
    )

    val left = Sphere(
        transform = translation(-4.5, -3.0, 1.5) * scaling(1.5, 0.5, 3.5),
        material = Material(
            color = color(0.4, 0.4, 0.4),
            diffuse = 0.7,
            specular = 0.0
        )
    )

    val lightSource = PointLight(point(0, 4, 0), WHITE)
    val world = World(listOf(pondFloor, pondSurface, right, left), listOf(lightSource))

    val transform = viewTransform(point(0.0, 5.0, -5.0), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 300, PI / 2, transform)

    return camera.render(world)
}
