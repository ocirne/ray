package io.github.ocirne.ray.challenge.raysphere

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector
import io.github.ocirne.ray.challenge.tuples.point
import kotlin.math.sqrt

class Sphere(val transform: Matrix = identityMatrix, val material: Material = Material()) {

    fun intersect(ray: Ray): Array<Intersection> {
        val ray2 = ray.transform(transform.inverse())

        // the vector from the sphere's center, to the ray origin
        // remember: the sphere is centered at the world origin
        val sphereToRay = ray2.origin - point(0, 0, 0)
        val a = ray2.direction.dot(ray2.direction)
        val b = 2 * ray2.direction.dot(sphereToRay)
        val c = sphereToRay.dot(sphereToRay) - 1
        val discriminant = b * b - 4 * a * c
        if (discriminant < 0) {
            return arrayOf()
        }
        val t1 = (-b - sqrt(discriminant)) / (2 * a)
        val t2 = (-b + sqrt(discriminant)) / (2 * a)
        return arrayOf(Intersection(t1, this), Intersection(t2, this))
    }

    fun withTransform(t: Matrix): Sphere {
        return Sphere(t)
    }

    fun withMaterial(m: Material): Sphere {
        return Sphere(material = m)
    }

    fun normalAt(worldPoint: Point): Vector {
        val objectPoint = transform.inverse() * worldPoint
        val objectNormal = objectPoint - point(0, 0, 0)
        val worldNormal = transform.inverse().transpose() * objectNormal
        return worldNormal.ensureVector().normalize()
    }
}