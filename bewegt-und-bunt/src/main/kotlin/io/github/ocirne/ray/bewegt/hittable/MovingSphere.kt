package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.material.Material
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Vector3.Companion.times
import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.math.sqrt

class MovingSphere(
    private val center0: Point3,
    private val center1: Point3,
    private val time0: Double,
    private val time1: Double,
    private val radius: Double,
    private val material: Material
) : Hittable() {

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        val oc = r.origin - center(r.time)
        val a = r.direction.lengthSquared()
        val halfB = oc.dot(r.direction)
        val c = oc.lengthSquared() - radius * radius

        val discriminant = halfB * halfB - a * c
        if (discriminant < 0) return null
        val sqrtDiscriminant = sqrt(discriminant)

        // Find the nearest root that lies in the acceptable range.
        var root = (-halfB - sqrtDiscriminant) / a
        if (root < tMin || tMax < root) {
            root = (-halfB + sqrtDiscriminant) / a
            if (root < tMin || tMax < root)
                return null
        }
        val p = r.at(root)
        val outwardNormal = (r.at(root) - center(r.time)) / radius
        val frontFace = r.direction.dot(outwardNormal) < 0
        val normal = if (frontFace) outwardNormal else -outwardNormal
        val (u, v) = Sphere.getSphereUv(outwardNormal)

        return HitRecord(p, normal, material, root, u, v, frontFace)
    }

    override fun boundingBox(time0: Double, time1: Double): AABB {
        val box0 = AABB(
            center(time0) - Vector3(radius, radius, radius),
            center(time0) + Vector3(radius, radius, radius),
        )
        val box1 = AABB(
            center(time1) - Vector3(radius, radius, radius),
            center(time1) + Vector3(radius, radius, radius),
        )
        return surroundingBox(box0, box1)
    }

    private fun center(time: Double): Point3 {
        return center0 + ((time - time0) / (time1 - time0)) * (center1 - center0)
    }
}