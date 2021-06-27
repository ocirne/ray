package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.material.Material
import io.github.ocirne.ray.bewegt.math.*
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

class Sphere(private val center: Point3, private val radius: Double, private val material: Material) : Hittable() {

    constructor(center: Point3, radius: Int, material: Material) : this(center, radius.toDouble(), material)

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        val oc = r.origin - center
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
        val outwardNormal = (r.at(root) - center) / radius
        val (u, v) = getSphereUv(outwardNormal)

        return directHit(r, material, root, u, v, outwardNormal)
    }

    override fun boundingBox(time0: Double, time1: Double): AABB {
        return AABB(center - Vector3(radius, radius, radius), center + Vector3(radius, radius, radius))
    }

    override fun pdfValue(origin: Point3, v: Vector3): Double {
        hit(Ray(origin, v), 0.001, infinity) ?: return 0.0
        val cosThetaMax = sqrt(1 - radius * radius / (center - origin).lengthSquared())
        val solidAngle = 2 * PI * (1 - cosThetaMax)
        return 1.0 / solidAngle
    }

    override fun random(origin: Vector3): Vector3 {
        val direction = center - origin
        val distanceSquared = direction.lengthSquared()
        val uvw = ONB.buildFromW(direction)
        return uvw.local(randomToSphere(radius, distanceSquared))
    }

    companion object {
        fun getSphereUv(p: Point3): Pair<Double, Double> {
            // p: a given point on the sphere of radius one, centered at the origin.
            // u: returned value [0,1] of angle around the Y axis from X=-1.
            // v: returned value [0,1] of angle from Y=-1 to Y=+1.
            //     <1 0 0> yields <0.50 0.50>       <-1  0  0> yields <0.00 0.50>
            //     <0 1 0> yields <0.50 1.00>       < 0 -1  0> yields <0.50 0.00>
            //     <0 0 1> yields <0.25 0.50>       < 0  0 -1> yields <0.75 0.50>

            val theta = acos(-p.y)
            val phi = atan2(-p.z, p.x) + PI

            val u = phi / (2 * PI)
            val v = theta / PI
            return Pair(u, v)
        }
    }
}
