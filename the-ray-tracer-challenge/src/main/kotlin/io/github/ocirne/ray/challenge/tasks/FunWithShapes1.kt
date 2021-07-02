package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.patterns.CheckersPattern
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.*
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI

fun funWithShapes1(): Canvas {
    val woodMaterial = Material(color = BROWN)
    val fieldMaterial = Material(pattern = CheckersPattern(BLACK, WHITE, scaling(0.25, 1.0, 0.25).rotateY(PI/2)))
    val whiteMaterial = Material(color = ELFENBEIN)
    val blackMaterial = Material(color = DARK_GRAY, specular = 0.5)
    val mirrorMaterial = Material(color = color(0.8, 0.8, 0.8), reflective = 1.0)

    val shapes = ArrayList<Shape>()
    // Brett
    shapes.add(Cube(scaling(13.0, 2.0, 0.5).translate(0.0, -2.5, -12.5), material = woodMaterial))
    shapes.add(Cube(scaling(13.0, 2.0, 0.5).translate(0.0, -2.5, 12.5), material = woodMaterial))
    shapes.add(Cube(scaling(0.5, 2.0, 13.0).translate(-12.5, -2.5, 0.0), material = woodMaterial))
    shapes.add(Cube(scaling(0.5, 2.0, 13.0).translate(12.5, -2.5, 0.0), material = woodMaterial))
    shapes.add(Cube(scaling(12, 1, 12).translate(0.0, -2.0, 0.0), material = fieldMaterial))

    // Figuren (Abstand: 3.0)
    for (b in 1..8) {
        shapes.add(Sphere(moveTo(b, 2), whiteMaterial))
        shapes.add(Sphere(moveTo(b, 7), blackMaterial))
    }
    // Türme
    shapes.add(Cube(moveTo(1, 1), whiteMaterial))
    shapes.add(Cube(moveTo(8, 1), whiteMaterial))
    shapes.add(Cube(moveTo(1, 8), blackMaterial))
    shapes.add(Cube(moveTo(8, 8), blackMaterial))

    // Damen
    shapes.add(Cone(moveTo(5, 1).scale(1.0, 2.0, 1.0).translate(0.0, 1.0, 0.0), whiteMaterial, minimum = -1.0, maximum = 0.0))
    shapes.add(Cone(moveTo(5, 8).scale(1.0, 2.0, 1.0).translate(0.0, 1.0, 0.0), blackMaterial, minimum = -1.0, maximum = 0.0))

    // Könige
    shapes.add(Cone(moveTo(4, 1).scale(1.0, 2.0, 1.0).translate(0.0, 1.0, 0.0), whiteMaterial, minimum = -1.0, maximum = 0.5))
    shapes.add(Cone(moveTo(4, 8).scale(1.0, 2.0, 1.0).translate(0.0, 1.0, 0.0), blackMaterial, minimum = -1.0, maximum = 0.5))

    // Läufer
    shapes.add(Cylinder(moveTo(3, 1), whiteMaterial, minimum = -1.0, maximum = 1.0, closed = true))
    shapes.add(Cylinder(moveTo(6, 1), whiteMaterial, minimum = -1.0, maximum = 1.0, closed = true))
    shapes.add(Cylinder(moveTo(3, 8), blackMaterial, minimum = -1.0, maximum = 1.0, closed = true))
    shapes.add(Cylinder(moveTo(6, 8), blackMaterial, minimum = -1.0, maximum = 1.0, closed = true))

    // Springer
    // TODO

    // Spiegel
    shapes.add(Cube(scaling(3.0, 1.0, 10.0).rotateZ(PI/2).translate(-15.0, 3.0, 0.0), material = mirrorMaterial))

    val lightSource = PointLight(point(-5, 10, -5), WHITE)
    val world = World(shapes.toList(), listOf(lightSource))

    val transform = viewTransform(point(10.0, 5.0, -20.0), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(1680, 1050, PI / 2, transform)

    return camera.render(world)
}

fun moveTo(x: Int, z: Int): Matrix {
    return translation(x * 3.0 - 13.5, 0.0, z * 3.0 - 13.5)
}
