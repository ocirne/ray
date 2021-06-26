package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.material.Material
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Vector3.Companion.times
import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.math.sqrt

class moving_sphere(
    val center0: Point3,
    val center1: Point3,
    val time0: Double,
    val time1: Double,
    val radius: Double,
    val mat: Material
) : Hittable() {

    override fun hit(r: Ray, t_min: Double, t_max: Double): HitRecord? {
        val oc = r.origin - center(r.time)
        val a = r.direction.lengthSquared()
        val half_b = oc.dot(r.direction)
        val c = oc.lengthSquared() - radius * radius

        val discriminant = half_b * half_b - a * c
        if (discriminant < 0) return null
        val sqrtd = sqrt(discriminant)

        // Find the nearest root that lies in the acceptable range.
        var root = (-half_b - sqrtd) / a
        if (root < t_min || t_max < root) {
            root = (-half_b + sqrtd) / a
            if (root < t_min || t_max < root)
                return null
        }
        val p = r.at(root)
        val outward_normal = (r.at(root) - center(r.time)) / radius
        val front_face = r.direction.dot(outward_normal) < 0
        val normal = if (front_face) outward_normal else -outward_normal
        val (u, v) = sphere.get_sphere_uv(outward_normal)

        return HitRecord(p, normal, mat, root, u, v, front_face)
    }

    override fun boundingBox(time0: Double, time1: Double): aabb {
        val box0 = aabb(
            center(time0) - Vector3(radius, radius, radius),
            center(time0) + Vector3(radius, radius, radius),
        )
        val box1 = aabb(
            center(time1) - Vector3(radius, radius, radius),
            center(time1) + Vector3(radius, radius, radius),
        )
        return surrounding_box(box0, box1)
    }

    fun center(time: Double): Point3 {
        return center0 + ((time - time0) / (time1 - time0))*(center1 - center0)
    }
}