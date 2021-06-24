package io.github.ocirne.ray.bewegt

import kotlin.math.sqrt

class moving_sphere(val center0: point3,
                    val center1: point3,
                    val time0: Double,
                    val time1: Double,
                    val radius: Double,
                    val mat: material): hittable {

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        val oc = r.origin() - center(r.time())
        val a = r.direction().length_squared()
        val half_b = oc.dot(r.direction())
        val c = oc.length_squared() - radius * radius

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
        val outward_normal = (r.at(root) - center(r.time())) / radius
        val front_face = r.direction().dot(outward_normal) < 0
        val normal = if (front_face) outward_normal else -outward_normal
        val (u, v) = sphere.get_sphere_uv(outward_normal)

        return hit_record(p, normal, mat, root, u, v, front_face)
    }

    override fun bounding_box(time0: Double, time1: Double): aabb {
        val box0 = aabb(
            center(time0) - vec3(radius, radius, radius),
            center(time0) + vec3(radius, radius, radius),
        )
        val box1 = aabb(
            center(time1) - vec3(radius, radius, radius),
            center(time1) + vec3(radius, radius, radius),
        )
        return surrounding_box(box0, box1)
    }

    fun center(time: Double): point3 {
        return center0 + ((time - time0) / (time1 - time0))*(center1 - center0)
    }
}