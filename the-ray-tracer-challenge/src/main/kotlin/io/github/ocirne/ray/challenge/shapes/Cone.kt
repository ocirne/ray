package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.math.epsilon
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector
import io.github.ocirne.ray.challenge.tuples.point
import io.github.ocirne.ray.challenge.tuples.vector
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.*

data class Cone(
    override val transform: Matrix = identityMatrix,
    override var material: Material = Material(),
    val maximum: Double = POSITIVE_INFINITY,
    val minimum: Double = NEGATIVE_INFINITY,
    val closed: Boolean = false
) : Shape(transform, material) {

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val xs = mutableListOf<Intersection>()
        intersectWalls(localRay, xs)
        intersectCaps(localRay, xs)
        return xs.toList()
    }

    private fun intersectWalls(ray: Ray, xs: MutableList<Intersection>) {
        val a = ray.direction.x.pow(2) - ray.direction.y.pow(2) + ray.direction.z.pow(2)
        val b =
            2 * ray.origin.x * ray.direction.x - 2 * ray.origin.y * ray.direction.y + 2 * ray.origin.z * ray.direction.z
        if (abs(a) < epsilon && abs(b) < epsilon) {
            return
        }
        val c = ray.origin.x.pow(2) - ray.origin.y.pow(2) + ray.origin.z.pow(2)

        if (abs(a) < epsilon) {
            val t = -c / (2 * b)
            xs.add(Intersection(t, this))
            return
        }
        val disc = b * b - 4 * a * c

        // ray does not intersect the Cylinder
        if (disc < 0) {
            return
        }
        var t0 = (-b - sqrt(disc)) / (2 * a)
        var t1 = (-b + sqrt(disc)) / (2 * a)

        if (t0 > t1) {
            t0 = t1.also { t1 = t0 }
        }
        val y0 = ray.origin.y + t0 * ray.direction.y
        if (minimum < y0 && y0 < maximum) {
            xs.add(Intersection(t0, this))
        }
        val y1 = ray.origin.y + t1 * ray.direction.y
        if (minimum < y1 && y1 < maximum) {
            xs.add(Intersection(t1, this))
        }
    }

    private fun checkCap(ray: Ray, t: Double, y: Double): Boolean {
        val x = ray.origin.x + t * ray.direction.x
        val z = ray.origin.z + t * ray.direction.z
        return (x * x + z * z) <= y
    }

    private fun intersectCaps(ray: Ray, xs: MutableList<Intersection>) {
        // caps only matter if the Cylinder is closed, and might possibly be
        // intersected by the ray.
        if (!closed || abs(ray.direction.y) < epsilon) {
            return
        }

        // check for an intersection with the lower end cap by intersecting
        // the ray with the plane at y=cyl.minimum
        val t1 = (minimum - ray.origin.y) / ray.direction.y
        if (checkCap(ray, t1, abs(minimum))) {
            xs.add(Intersection(t1, this))
        }

        // check for an intersection with the upper end cap by intersecting
        // the ray with the plane at y=cyl.maximum
        val t2 = (maximum - ray.origin.y) / ray.direction.y
        if (checkCap(ray, t2, abs(maximum))) {
            xs.add(Intersection(t2, this))
        }
    }

    override fun localNormalAt(point: Point, hit: Intersection?): Vector {
        // compute the square of the distance from the y axis
        val dist = point.x * point.x + point.z * point.z

        return if (dist < 1 && point.y >= maximum - epsilon) {
            vector(0, 1, 0)
        } else if (dist < 1 && point.y <= minimum + epsilon) {
            vector(0, -1, 0)
        } else {
            val y = -sign(point.y) * sqrt(point.x * point.x + point.z * point.z)
            vector(point.x, y, point.z)
        }
    }

    override fun bounds(): Bounds {
        val lowerBound = max(minimum, NEGATIVE_INFINITY)
        val upperBound = min(maximum, POSITIVE_INFINITY)
        return Bounds(
            point(lowerBound, lowerBound, lowerBound),
            point(upperBound, upperBound, upperBound)
        )
    }
}
