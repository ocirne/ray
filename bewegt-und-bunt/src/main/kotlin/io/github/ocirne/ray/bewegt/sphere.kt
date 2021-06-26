package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.material.Material
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

class sphere(val center: Point3, val radius: Double, val mat: Material): hittable {

    constructor(center: Point3, radius: Int, mat: Material): this(center, radius.toDouble(), mat)

    override fun hit(r: Ray, t_min: Double, t_max: Double): HitRecord? {
        val oc = r.origin - center
        val a = r.direction.lengthSquared()
        val half_b = oc.dot(r.direction)
        val c = oc.lengthSquared() - radius*radius

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
        val front_face = r.direction.dot(outward_normal) < 0
        val normal = if (front_face) outward_normal else -outward_normal
        val (u, v) = get_sphere_uv(outward_normal)

        return HitRecord(p, normal, mat, root, u, v, front_face)
    }

    override fun bounding_box(time0: Double, time1: Double): aabb {
        return aabb(center - Vector3(radius, radius, radius), center + Vector3(radius, radius, radius))
    }

    override fun pdf_value(origin: Point3, v: Vector3): Double {
        hit(Ray(origin, v), 0.001, infinity) ?: return 0.0
        val cos_theta_max = sqrt(1 - radius*radius/(center-origin).lengthSquared())
        val solid_angle = 2*PI*(1-cos_theta_max)
        return 1.0 / solid_angle
    }

    override fun random(origin: Vector3): Vector3 {
        val direction = center - origin
        val distance_squared = direction.lengthSquared()
        val uvw = onb.build_from_w(direction)
        return uvw.local(random_to_sphere(radius, distance_squared))
    }

    companion object {
        fun get_sphere_uv(p: Point3): Pair<Double, Double> {
            // p: a given point on the sphere of radius one, centered at the origin.
            // u: returned value [0,1] of angle around the Y axis from X=-1.
            // v: returned value [0,1] of angle from Y=-1 to Y=+1.
            //     <1 0 0> yields <0.50 0.50>       <-1  0  0> yields <0.00 0.50>
            //     <0 1 0> yields <0.50 1.00>       < 0 -1  0> yields <0.50 0.00>
            //     <0 0 1> yields <0.25 0.50>       < 0  0 -1> yields <0.75 0.50>

            val theta = acos(-p.y)
            val phi = atan2(-p.z, p.x) + PI

            val u = phi / (2*PI)
            val v = theta / PI
            return Pair(u, v)
        }
    }
}
