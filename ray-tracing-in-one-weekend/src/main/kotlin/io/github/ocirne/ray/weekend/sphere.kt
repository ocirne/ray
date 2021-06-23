package io.github.ocirne.ray.weekend

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

class sphere(val center: point3, val radius: Double, val mat: material): hittable {

    constructor(center: point3, radius: Int, mat: material): this(center, radius.toDouble(), mat)

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        val oc = r.origin() - center
        val a = r.direction().length_squared()
        val half_b = oc.dot(r.direction())
        val c = oc.length_squared() - radius*radius

        val discriminant = half_b*half_b - a*c
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
        val outward_normal = (r.at(root) - center) / radius
        val front_face = r.direction().dot(outward_normal) < 0
        val normal = if (front_face) outward_normal else -outward_normal
        val (u, v) = get_sphere_uv(outward_normal)

        return hit_record(p, normal, mat, root, u, v, front_face)
    }

    override fun bounding_box(time0: Double, time1: Double): aabb {
        return aabb(center - vec3(radius, radius, radius), center + vec3(radius, radius, radius))
    }

    companion object {
        fun get_sphere_uv(p: point3): Pair<Double, Double> {
            // p: a given point on the sphere of radius one, centered at the origin.
            // u: returned value [0,1] of angle around the Y axis from X=-1.
            // v: returned value [0,1] of angle from Y=-1 to Y=+1.
            //     <1 0 0> yields <0.50 0.50>       <-1  0  0> yields <0.00 0.50>
            //     <0 1 0> yields <0.50 1.00>       < 0 -1  0> yields <0.50 0.00>
            //     <0 0 1> yields <0.25 0.50>       < 0  0 -1> yields <0.75 0.50>

            val theta = acos(-p.y())
            val phi = atan2(-p.z(), p.x()) + PI

            val u = phi / (2*PI)
            val v = theta / PI
            return Pair(u, v)
        }
    }
}
