package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.material.Material
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Ray
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.infinity
import kotlin.math.abs
import kotlin.random.Random

class xy_rect(val x0: Double, val x1: Double, val y0: Double, val y1: Double, val k: Double, val material: Material) :
    Hittable() {

    constructor(x0: Int, x1: Int, y0: Int, y1: Int, k: Int, material: Material):
            this(x0.toDouble(), x1.toDouble(), y0.toDouble(), y1.toDouble(), k.toDouble(), material)

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        val t = (k-r.origin.z) / r.direction.z
        if (t < tMin || t > tMax) {
            return null
        }
        val x = r.origin.x + t*r.direction.x
        val y = r.origin.y + t*r.direction.y
        if (x < x0 || x > x1 || y < y0 || y > y1) {
            return null
        }
        val u = (x-x0)/(x1-x0)
        val v = (y-y0)/(y1-y0)
        val outwardNormal = Vector3(0, 0, 1)

        return directHit(r, material, t, u, v, outwardNormal)
    }

    override fun boundingBox(time0: Double, time1: Double): AABB {
        // The bounding box must have non-zero width in each dimension, so pad the Z
        // dimension a small amount.
        return AABB(Point3(x0, y0, k-0.0001), Point3(x1, y1, k+0.0001))
    }
}

class xz_rect(val x0: Double, val x1: Double, val z0: Double, val z1: Double, val k: Double, val material: Material) :
    Hittable() {

    constructor(x0: Int, x1: Int, z0: Int, z1: Int, k: Int, material: Material):
            this(x0.toDouble(), x1.toDouble(), z0.toDouble(), z1.toDouble(), k.toDouble(), material)

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        val t = (k-r.origin.y) / r.direction.y
        if (t < tMin || t > tMax) {
            return null
        }
        val x = r.origin.x + t*r.direction.x
        val z = r.origin.z + t*r.direction.z
        if (x < x0 || x > x1 || z < z0 || z > z1) {
            return null
        }
        val u = (x-x0)/(x1-x0)
        val v = (z-z0)/(z1-z0)
        val outwardNormal = Vector3(0, 1, 0)

        return directHit(r, material, t, u, v, outwardNormal)
    }

    override fun boundingBox(time0: Double, time1: Double): AABB {
        // The bounding box must have non-zero width in each dimension, so pad the Y
        // dimension a small amount.
        return AABB(Point3(x0, k-0.0001, z0), Point3(x1, k+0.0001, z1))
    }

    override fun pdfValue(origin: Point3, v: Vector3): Double {
        val rec = hit(Ray(origin, v), 0.001, infinity) ?: return 0.0

        val area = (x1-x0)*(z1-z0)
        val distanceSquared = rec.t * rec.t * v.lengthSquared()
        val cosine = abs(v.dot(rec.normal) / v.length())

        return distanceSquared / (cosine * area)
    }

    override fun random(origin: Vector3): Vector3 {
        val randomPoint = Point3(Random.nextDouble(x0, x1), k, Random.nextDouble(z0, z1))
        return randomPoint - origin
    }
}

class yz_rect(val y0: Double, val y1: Double, val z0: Double, val z1: Double, val k: Double, val material: Material) :
    Hittable() {

    constructor(y0: Int, y1: Int, z0: Int, z1: Int, k: Int, material: Material):
            this(y0.toDouble(), y1.toDouble(), z0.toDouble(), z1.toDouble(), k.toDouble(), material)

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        val t = (k-r.origin.x) / r.direction.x
        if (t < tMin || t > tMax) {
            return null
        }
        val y = r.origin.y + t*r.direction.y
        val z = r.origin.z + t*r.direction.z
        if (y < y0 || y > y1 || z < z0 || z > z1) {
            return null
        }
        val u = (y-y0)/(y1-y0)
        val v = (z-z0)/(z1-z0)
        val outwardNormal = Vector3(1, 0, 0)

        return directHit(r, material, t, u, v, outwardNormal)
    }

    override fun boundingBox(time0: Double, time1: Double): AABB {
        // The bounding box must have non-zero width in each dimension, so pad the X
        // dimension a small amount.
        return AABB(Point3(k-0.0001, y0, z0), Point3(k+0.0001, y1, z1))
    }
}
