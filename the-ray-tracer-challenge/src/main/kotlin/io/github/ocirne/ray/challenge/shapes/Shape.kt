package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector

abstract class Shape(open val transform: Matrix = identityMatrix,
                     open val material: Material = Material()) {

    fun intersect(ray: Ray): List<Intersection> {
        val localRay = ray.transform(transform.inverse())
        return localIntersect(localRay)
    }

    abstract fun localIntersect(localRay: Ray): List<Intersection>

    fun normalAt(worldPoint: Point): Vector {
        val localPoint = transform.inverse() * worldPoint
        val localNormal = localNormalAt(localPoint)
        val worldNormal = transform.inverse().transpose() * localNormal
        return worldNormal.ensureVector().normalize()
    }

    abstract fun localNormalAt(localPoint: Point): Vector
}
