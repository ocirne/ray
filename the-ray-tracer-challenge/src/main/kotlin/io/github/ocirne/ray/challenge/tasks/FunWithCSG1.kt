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

fun funWithCSG1(): Canvas {

    val floorMaterial = Material(color = WHITE, specular = 0.0, pattern = CheckersPattern(DARK_GRAY, WHITE, transform = scaling(3, 3, 3)))
    val cubeMaterial = Material(color = YELLOW, specular = 0.0)
    val sphereMaterial = Material(color = RED, specular = 0.0)

    val floor = Plane(transform = translation(0, -2, 0), material = floorMaterial)

    val cube = Cube(material = cubeMaterial)
    val sphere = Sphere(translation(0.5, 0.5, -0.5), sphereMaterial)
    val csg1 = CSG(Operation.UNION, cube, sphere)
    val csg3 = CSG(Operation.DIFFERENCE, cube, sphere, transform = rotationY(PI/2).translate(3.0, 0.0, 0.0))
    val csg2 = CSG(Operation.INTERSECTION, cube, sphere, transform = scaling(-1, -1, -1).translate(6.0, 0.0, 0.0))
    val csg4 = CSG(Operation.DIFFERENCE, sphere, cube, transform = scaling(-1, -1, -1).translate(9.0, 0.0, 0.0))

    val lightSource = PointLight(point(4.5, 4.5, -3.0), WHITE)
    val world = World(listOf(floor, csg1, csg2, csg3, csg4), listOf(lightSource))

    val transform = viewTransform(point(4.5, 5.0, -5.0), point(4.5, 0.0, 0.0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 2.5, transform)

    return camera.render(world)
}
