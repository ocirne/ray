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

data class Cylinder(
    override val transform: Matrix = identityMatrix,
    override val material: Material = Material(),
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
        val a = ray.direction.x.pow(2) + ray.direction.z.pow(2)
        // ray is parallel to the y axis
        if (abs(a) < epsilon) {
            return
        }
        val b = 2 * ray.origin.x * ray.direction.x + 2 * ray.origin.z * ray.direction.z
        val c = ray.origin.x.pow(2) + ray.origin.z.pow(2) - 1

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

    /** a helper function to reduce duplication.
     * checks to see if the intersection at `t` is within a radius
     * of 1 (the radius of your Cylinders) from the y axis.
     */
    private fun checkCap(ray: Ray, t: Double): Boolean {
        val x = ray.origin.x + t * ray.direction.x
        val z = ray.origin.z + t * ray.direction.z
        return (x * x + z * z) <= 1
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
        if (checkCap(ray, t1)) {
            xs.add(Intersection(t1, this))
        }

        // check for an intersection with the upper end cap by intersecting
        // the ray with the plane at y=cyl.maximum
        val t2 = (maximum - ray.origin.y) / ray.direction.y
        if (checkCap(ray, t2)) {
            xs.add(Intersection(t2, this))
        }
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection?): Vector {
        // compute the square of the distance from the y axis
        val dist = localPoint.x * localPoint.x + localPoint.z * localPoint.z

        return if (dist < 1 && localPoint.y >= maximum - epsilon) {
            vector(0, 1, 0)
        } else if (dist < 1 && localPoint.y <= minimum + epsilon) {
            vector(0, -1, 0)
        } else {
            vector(localPoint.x, 0.0, localPoint.z)
        }
    }

    override fun bounds(): Bounds {
        val lowerBound = max(minimum, NEGATIVE_INFINITY)
        val upperBound = min(maximum, POSITIVE_INFINITY)
        return Bounds(
            point(-1.0, lowerBound, -1.0),
            point(1.0, upperBound, 1.0)
        )
    }
}
