package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector
import io.github.ocirne.ray.challenge.tuples.point
import io.github.ocirne.ray.challenge.tuples.vector
import kotlin.math.abs

data class Cube(
    override val transform: Matrix = identityMatrix,
    override val material: Material = Material()
) : Shape(transform, material) {

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val (xtmin, xtmax) = checkAxis(localRay.origin.x, localRay.direction.x)
        val (ytmin, ytmax) = checkAxis(localRay.origin.y, localRay.direction.y)
        val (ztmin, ztmax) = checkAxis(localRay.origin.z, localRay.direction.z)
        val tmin = maxOf(xtmin, ytmin, ztmin)
        val tmax = minOf(xtmax, ytmax, ztmax)
        return if (tmin <= tmax) listOf(Intersection(tmin, this), Intersection(tmax, this)) else listOf()
    }

    private fun checkAxis(origin: Double, direction: Double): Pair<Double, Double> {
        val tmin = (-1 - origin) / direction
        val tmax = (1 - origin) / direction
        return if (tmin > tmax) Pair(tmax, tmin) else Pair(tmin, tmax)
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection?): Vector {
        val maxc = maxOf(abs(localPoint.x), abs(localPoint.y), abs(localPoint.z))

        return when (maxc) {
            abs(localPoint.x) -> vector(localPoint.x, 0.0, 0.0)
            abs(localPoint.y) -> vector(0.0, localPoint.y, 0.0)
            else -> vector(0.0, 0.0, localPoint.z)
        }
    }

    override fun bounds(): Bounds {
        return Bounds(
            point(-1, -1, -1),
            point(1, 1, 1)
        )
    }
}