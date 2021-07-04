package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector
import io.github.ocirne.ray.challenge.tuples.point

data class Group(
    override val transform: Matrix = identityMatrix,
    override val material: Material = Material()
) : Shape(transform, material), Collection<Shape> {

    override var size = 0

    private var children = mutableSetOf<Shape>()

    private var bounds: Bounds? = null

    override fun localIntersect(localRay: Ray): List<Intersection> {
        if (children.isEmpty() || !hitsBoundingBox(localRay)) {
            return listOf()
        }
        return children
            .map { child -> child.intersect(localRay) }
            .flatten()
            .sortedBy { intersection -> intersection.t }
    }

    private fun hitsBoundingBox(localRay: Ray): Boolean {
        val bounds = bounds()
        val (xtmin, xtmax) = checkAxis(localRay.origin.x, localRay.direction.x, bounds.minimum.x, bounds.maximum.x)
        val (ytmin, ytmax) = checkAxis(localRay.origin.y, localRay.direction.y, bounds.minimum.y, bounds.maximum.y)
        val (ztmin, ztmax) = checkAxis(localRay.origin.z, localRay.direction.z, bounds.minimum.z, bounds.maximum.z)
        val tmin = maxOf(xtmin, ytmin, ztmin)
        val tmax = minOf(xtmax, ytmax, ztmax)
        return tmin <= tmax
    }

    private fun checkAxis(origin: Double, direction: Double, minA: Double, maxA: Double): Pair<Double, Double> {
        val tmin = (minA - origin) / direction
        val tmax = (maxA - origin) / direction
        return if (tmin > tmax) Pair(tmax, tmin) else Pair(tmin, tmax)
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

    override fun bounds(): Bounds {
        if (bounds == null) {
            val tuples = mutableListOf<Point>()
            for (child in children) {
                val bounds = child.bounds()
                for (corner in bounds.corners()) {
                    // object space to parent space
                    tuples.add(child.transform * corner)
                }
            }
            val minX = tuples.minOfOrNull { it.x }!!
            val minY = tuples.minOfOrNull { it.y }!!
            val minZ = tuples.minOfOrNull { it.z }!!
            val maxX = tuples.maxOfOrNull { it.x }!!
            val maxY = tuples.maxOfOrNull { it.y }!!
            val maxZ = tuples.maxOfOrNull { it.z }!!
            bounds = Bounds(point(minX, minY, minZ), point(maxX, maxY, maxZ))
        }
        return bounds!!
    }
}