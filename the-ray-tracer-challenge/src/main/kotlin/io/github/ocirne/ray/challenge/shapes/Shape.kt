package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector

abstract class Shape(
    open val transform: Matrix = identityMatrix,
    open val material: Material = Material(),
    var parent: Shape? = null
) {

    fun intersect(ray: Ray): List<Intersection> {
        val localRay = ray.transform(transform.inverse())
        return localIntersect(localRay)
    }

    abstract fun localIntersect(localRay: Ray): List<Intersection>

    fun normalAt(worldPoint: Point): Vector {
        val localPoint = worldToObject(worldPoint)
        val localNormal = localNormalAt(localPoint)
        return normalToWorld(localNormal)
    }

    abstract fun localNormalAt(localPoint: Point): Vector

    fun setParent(group: Group) {
        parent = group
    }

    fun worldToObject(worldPoint: Point): Point {
        val point = if (parent != null) parent!!.worldToObject(worldPoint) else worldPoint
        return transform.inverse() * point
    }

    fun normalToWorld(objectNormal: Vector): Vector {
        val normal1 = transform.inverse().transpose() * objectNormal
        val normal2 = normal1.ensureVector().normalize()

        return if (parent != null) parent!!.normalToWorld(normal2) else normal2
    }

    abstract fun bounds(): Bounds
}
