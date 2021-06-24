package io.github.ocirne.ray.bewegt

import kotlin.math.abs
import kotlin.random.Random

class xy_rect(val x0: Double, val x1: Double, val y0: Double, val y1: Double, val k: Double, val mat: material) : hittable {

    constructor(x0: Int, x1: Int, y0: Int, y1: Int, k: Int, mat: material):
            this(x0.toDouble(), x1.toDouble(), y0.toDouble(), y1.toDouble(), k.toDouble(), mat)

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        val t = (k-r.origin().z()) / r.direction().z()
        if (t < t_min || t > t_max) {
            return null
        }
        val x = r.origin().x() + t*r.direction().x()
        val y = r.origin().y() + t*r.direction().y()
        if (x < x0 || x > x1 || y < y0 || y > y1) {
            return null
        }
        val u = (x-x0)/(x1-x0)
        val v = (y-y0)/(y1-y0)
        val outward_normal = vec3(0, 0, 1)
        val front_face = r.direction().dot(outward_normal) < 0
        val normal = if (front_face) outward_normal else -outward_normal
        val p = r.at(t)

        return hit_record(p, normal, mat, t, u, v, front_face)
    }

    override fun bounding_box(time0: Double, time1: Double): aabb {
        // The bounding box must have non-zero width in each dimension, so pad the Z
        // dimension a small amount.
        return aabb(point3(x0, y0, k-0.0001), point3(x1, y1, k+0.0001))
    }
}

class xz_rect(val x0: Double, val x1: Double, val z0: Double, val z1: Double, val k: Double, val mat: material) : hittable {

    constructor(x0: Int, x1: Int, z0: Int, z1: Int, k: Int, mat: material):
            this(x0.toDouble(), x1.toDouble(), z0.toDouble(), z1.toDouble(), k.toDouble(), mat)

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        val t = (k-r.origin().y()) / r.direction().y()
        if (t < t_min || t > t_max) {
            return null
        }
        val x = r.origin().x() + t*r.direction().x()
        val z = r.origin().z() + t*r.direction().z()
        if (x < x0 || x > x1 || z < z0 || z > z1) {
            return null
        }
        val u = (x-x0)/(x1-x0)
        val v = (z-z0)/(z1-z0)
        val outward_normal = vec3(0, 1, 0)
        val front_face = r.direction().dot(outward_normal) < 0
        val normal = if (front_face) outward_normal else -outward_normal
        val p = r.at(t)

        return hit_record(p, normal, mat, t, u, v, front_face)
    }

    override fun bounding_box(time0: Double, time1: Double): aabb {
        // The bounding box must have non-zero width in each dimension, so pad the Y
        // dimension a small amount.
        return aabb(point3(x0, k-0.0001, z0), point3(x1, k+0.0001, z1))
    }

    override fun pdf_value(origin: point3, v: vec3): Double {
        val rec = hit(ray(origin, v), 0.001, infinity) ?: return 0.0

        val area = (x1-x0)*(z1-z0)
        val distance_squared = rec.t * rec.t * v.length_squared()
        val cosine = abs(v.dot(rec.normal) / v.length())

        return distance_squared / (cosine * area)
    }

    override fun random(origin: vec3): vec3 {
        val random_point = point3(Random.nextDouble(x0, x1), k, Random.nextDouble(z0, z1))
        return random_point - origin
    }
}

class yz_rect(val y0: Double, val y1: Double, val z0: Double, val z1: Double, val k: Double, val mat: material) : hittable {

    constructor(y0: Int, y1: Int, z0: Int, z1: Int, k: Int, mat: material):
            this(y0.toDouble(), y1.toDouble(), z0.toDouble(), z1.toDouble(), k.toDouble(), mat)

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        val t = (k-r.origin().x()) / r.direction().x()
        if (t < t_min || t > t_max) {
            return null
        }
        val y = r.origin().y() + t*r.direction().y()
        val z = r.origin().z() + t*r.direction().z()
        if (y < y0 || y > y1 || z < z0 || z > z1) {
            return null
        }
        val u = (y-y0)/(y1-y0)
        val v = (z-z0)/(z1-z0)
        val outward_normal = vec3(1, 0, 0)
        val front_face = r.direction().dot(outward_normal) < 0
        val normal = if (front_face) outward_normal else -outward_normal
        val p = r.at(t)

        return hit_record(p, normal, mat, t, u, v, front_face)
    }

    override fun bounding_box(time0: Double, time1: Double): aabb {
        // The bounding box must have non-zero width in each dimension, so pad the X
        // dimension a small amount.
        return aabb(point3(k-0.0001, y0, z0), point3(k+0.0001, y1, z1))
    }
}
