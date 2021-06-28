package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.material.Material
import io.github.ocirne.ray.bewegt.math.*
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

data class HitRecord(
    val p: Point3,
    val normal: Vector3,
    val material: Material,
    val t: Double,
    val u: Double,
    val v: Double,
    val frontFace: Boolean,
) {

    fun flippedFrontFace(): HitRecord {
        return HitRecord(p, normal, material, t, u, v, !frontFace)
    }

    fun translated(offset: Vector3, r: Ray): HitRecord {
        val frontFace = r.direction.dot(this.normal) < 0
        val normal = if (frontFace) this.normal else -this.normal
        return HitRecord(this.p + offset, normal, this.material, this.t, this.u, this.v, true)
    }

    fun rotated(p: Point3, r: Ray, outward_normal: Vector3): HitRecord {
        val frontFace = r.direction.dot(outward_normal) < 0
        return HitRecord(p, outward_normal, this.material, this.t, this.u, this.v, frontFace)
    }
}

fun directHit(r: Ray, material: Material, t: Double, u: Double, v: Double, outward_normal: Vector3): HitRecord {
    val p = r.at(t)
    val frontFace = r.direction.dot(outward_normal) < 0
    val normal = if (frontFace) outward_normal else -outward_normal

    return HitRecord(p, normal, material, t, u, v, frontFace)
}

fun bouncingHit(r: Ray, phaseFunction: Material, t: Double): HitRecord {
    val p = r.at(t)
    val u = 0.0
    val v = 0.0
    val normal = Vector3(1,0,0)  // arbitrary
    val frontFace = true     // also arbitrary

    return HitRecord(p, normal, phaseFunction, t, u, v, frontFace)
}

abstract class Hittable {

    abstract fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord?

    abstract fun boundingBox(time0: Double, time1: Double): AABB?

    open fun pdfValue(origin: Point3, v: Vector3): Double {
        return 0.0
    }

    open fun random(origin: Vector3): Vector3 {
        return Vector3(1, 0, 0)
    }

    fun translate(vector3: Vector3): Hittable {
        return Translation(this, vector3)
    }

    fun rotate(axis: Int, angle: Double): Hittable {
        return when (axis) {
            1 -> RotationY(this, angle)
            else -> throw NotImplementedError()
        }
    }

    fun flipFace(): Hittable {
        return FlipFace(this)
    }

    fun toConstantMedium(density: Double, color: RGBColor): Hittable {
        return ConstantMedium(this, density, color)
    }
}

private class Translation(private val delegate: Hittable, private val offset: Vector3) : Hittable() {

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        val translatedRay = Ray(r.origin - offset, r.direction, r.time)
        return delegate.hit(translatedRay, tMin, tMax)?.let {
            it.translated(offset, translatedRay)
        }
    }

    override fun boundingBox(time0: Double, time1: Double): AABB? {
        return delegate.boundingBox(time0, time1)?.let {
            AABB(it.minimum + offset, it.maximum + offset)
        }
    }
}

private class RotationY(val delegate: Hittable, angle: Double) : Hittable() {

    val sinTheta: Double
    val cosTheta: Double
    val bbox: AABB?

    init {
        val radians = angle.degreesToRadians()
        sinTheta = sin(radians)
        cosTheta = cos(radians)
        bbox = delegate.boundingBox(0.0, 1.0)?.let {
            var min = Point3(infinity, infinity, infinity)
            var max = Point3(-infinity, -infinity, -infinity)

            for (i in 0..1) {
                for (j in 0..1) {
                    for (k in 0..1) {
                        val x = i * it.maximum.x + (1 - i) * it.minimum.x
                        val y = j * it.maximum.y + (1 - j) * it.minimum.y
                        val z = k * it.maximum.z + (1 - k) * it.minimum.z

                        val newX = cosTheta * x + sinTheta * z
                        val newZ = -sinTheta * x + cosTheta * z

                        val tester = Vector3(newX, y, newZ)

                        min = Point3(
                            min(min.x, tester.x),
                            min(min.y, tester.y),
                            min(min.z, tester.z)
                        )
                        max = Point3(
                            max(max.x, tester.x),
                            max(max.y, tester.y),
                            max(max.z, tester.z)
                        )
                    }
                }
            }
            AABB(min, max)
        }
    }

    // TODO smells like rotation matrix
    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        val origin = Point3(
            cosTheta * r.origin.x - sinTheta * r.origin.z,
            r.origin.y,
            sinTheta * r.origin.x + cosTheta * r.origin.z
        )
        val direction = Vector3(
            cosTheta * r.direction.x - sinTheta * r.direction.z,
            r.direction.y,
            sinTheta * r.direction.x + cosTheta * r.direction.z
        )
        val rotatedRay = Ray(origin, direction, r.time)
        return delegate.hit(rotatedRay, tMin, tMax)?. let {
            val p = Point3(
                cosTheta * it.p.x + sinTheta * it.p.z,
                it.p.y,
                -sinTheta * it.p.x + cosTheta * it.p.z
            )
            val normal = Vector3(
                cosTheta * it.normal.x + sinTheta * it.normal.z,
                it.normal.y,
                -sinTheta * it.normal.x + cosTheta * it.normal.z
            )
            it.rotated(p, rotatedRay, normal)
        }
    }

    override fun boundingBox(time0: Double, time1: Double): AABB? {
        return bbox
    }
}

private class FlipFace(private val delegate: Hittable) : Hittable() {

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        return delegate.hit(r, tMin, tMax)?.flippedFrontFace()
    }

    override fun boundingBox(time0: Double, time1: Double): AABB? {
        return delegate.boundingBox(time0, time1)
    }
}
