package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector

data class Group(
    override val transform: Matrix = identityMatrix,
    override val material: Material = Material()
) : Shape(transform, material), Collection<Shape> {

    override var size = 0

    override fun localIntersect(localRay: Ray): List<Intersection> {
        TODO("No implementend")
    }

    override fun localNormalAt(localPoint: Point): Vector {
        TODO("No implementend")
    }

    fun addChild(shape: Shape) {
        TODO("No implementend")
    }

    override fun isEmpty(): Boolean {
        TODO()
    }

    override fun contains(element: Shape): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<Shape>): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<Shape> {
        TODO("Not yet implemented")
    }
}