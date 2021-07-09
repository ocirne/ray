package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.patterns.CheckersPattern
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.*
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI

fun funWithCSG2(): Canvas {

    val floorMaterial = Material(color = WHITE, specular = 0.0, pattern = CheckersPattern(DARK_GRAY, WHITE, transform = scaling(3, 3, 3)))
    val cubeMaterial = Material(color = GREEN)
    val pipsMaterial = Material(color = WHITE)

    val floor = Plane(transform = translation(0, -2, 0), material = floorMaterial)

    val cube = Cube(material = cubeMaterial)
    val pip1 = Sphere(transform= scaling(0.3, 0.3, 0.3).translate(0.5, 0.5, -1.2), material = pipsMaterial)
    val pip2 = Sphere(transform= scaling(0.3, 0.3, 0.3).translate(0.0, 0.0, -1.2), material = pipsMaterial)
    val pip3 = Sphere(transform= scaling(0.3, 0.3, 0.3).translate(-0.5, -0.5, -1.2), material = pipsMaterial)
    val dice = CSG(Operation.DIFFERENCE, cube, pip1)
    val dice2 = CSG(Operation.DIFFERENCE, dice, pip2)
    val dice3 = CSG(Operation.DIFFERENCE, dice2, pip3)
    val cyl = Cylinder(transform = scaling(0.2, 1.0, 0.2).translate(0.9, 0.0, -0.9), minimum = -1.2, maximum = 1.0, material = cubeMaterial)
    val dice4 = CSG(Operation.DIFFERENCE, dice3, cyl)

    val lightSource = PointLight(point(-4, 4, -4), WHITE)
    val world = World(listOf(floor, dice4), listOf(lightSource))

    val transform = viewTransform(point(0, 2, -5), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 2, transform)

    return camera.render(world)
}
