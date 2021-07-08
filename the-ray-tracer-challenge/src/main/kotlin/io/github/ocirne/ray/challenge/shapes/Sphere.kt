package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector
import io.github.ocirne.ray.challenge.tuples.point
import kotlin.math.sqrt

data class Sphere(
    override val transform: Matrix = identityMatrix,
    override val material: Material = Material()
) : Shape(transform, material) {

    override fun localIntersect(localRay: Ray): List<Intersection> {
        // the vector from the sphere's center, to the ray origin
        // remember: the sphere is centered at the world origin
        val sphereToRay = localRay.origin - point(0, 0, 0)
        val a = localRay.direction.dot(localRay.direction)
        val b = 2 * localRay.direction.dot(sphereToRay)
        val c = sphereToRay.dot(sphereToRay) - 1
        val discriminant = b * b - 4 * a * c
        if (discriminant < 0) {
            return listOf()
        }
        val t1 = (-b - sqrt(discriminant)) / (2 * a)
        val t2 = (-b + sqrt(discriminant)) / (2 * a)
        return listOf(Intersection(t1, this), Intersection(t2, this))
    }

    fun withTransform(t: Matrix): Sphere {
        return Sphere(t)
    }

    fun withMaterial(m: Material): Sphere {
        return Sphere(material = m)
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection?): Vector {
        return localPoint - point(0, 0, 0)
    }

    override fun bounds(): Bounds {
        return Bounds(point(-1, -1, -1), point(1, 1, 1))
    }
}

fun glassSphere(transform: Matrix = identityMatrix, refractiveIndex: Double = 1.5): Sphere {
    return Sphere(transform, Material(transparency = 1.0, refractiveIndex = refractiveIndex))
}
