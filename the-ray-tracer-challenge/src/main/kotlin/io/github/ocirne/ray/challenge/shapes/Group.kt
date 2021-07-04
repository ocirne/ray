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

    private var children = mutableSetOf<Shape>()

    override fun localIntersect(localRay: Ray): List<Intersection> {
        return children
            .map { child -> child.intersect(localRay) }
            .flatten()
            .sortedBy { intersection -> intersection.t }
    }

    override fun localNormalAt(localPoint: Point): Vector {
        TODO("This should never be called")
    }

    fun addChild(shape: Shape) {
        children.add(shape)
        shape.setParent(this)
        size = children.size
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun contains(element: Shape): Boolean {
        return children.contains(element)
    }

    override fun containsAll(elements: Collection<Shape>): Boolean {
        return children.containsAll(elements)
    }

    override fun iterator(): Iterator<Shape> {
        return children.iterator()
    }
}