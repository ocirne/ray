package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector
import io.github.ocirne.ray.challenge.tuples.vector

data class Cylinder(
    override val transform: Matrix = identityMatrix,
    override val material: Material = Material(),
    val maximum: Double = Double.POSITIVE_INFINITY,
    val minimum: Double = Double.NEGATIVE_INFINITY,
    val closed: Boolean = false
) : Shape(transform, material) {

    override fun localIntersect(localRay: Ray): List<Intersection> {
        return listOf()
    }

    override fun localNormalAt(localPoint: Point): Vector {
        return vector(0, 0, 0)
    }
}
