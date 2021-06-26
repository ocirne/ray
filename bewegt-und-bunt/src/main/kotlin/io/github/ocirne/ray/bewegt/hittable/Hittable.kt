package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.material.Material
import io.github.ocirne.ray.bewegt.math.*
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

data class HitRecord(
    var p: Point3,
    var normal: Vector3,
    val mat: Material,
    var t: Double,
    val u: Double,
    val v: Double,
    var frontFace: Boolean
) {

    fun setFaceNormal(r: Ray, outward_normal: Vector3) {
        frontFace = r.direction.dot(outward_normal) < 0
        normal = if (frontFace) outward_normal else -outward_normal
    }
}

abstract class Hittable {

    abstract fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord?

    abstract fun boundingBox(time0: Double, time1: Double): aabb?

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
        val movedRay = Ray(r.origin - offset, r.direction, r.time)
        return delegate.hit(movedRay, tMin, tMax)?.let {
            it.p += offset
            it.setFaceNormal(movedRay, it.normal)
            it
        }
    }

    override fun boundingBox(time0: Double, time1: Double): aabb? {
        return delegate.boundingBox(time0, time1)?.let {
            aabb(it.min() + offset, it.max() + offset)
        }
    }
}

private class RotationY(val delegate: Hittable, angle: Double) : Hittable() {

    val sinTheta: Double
    val cosTheta: Double
    val bbox: aabb?

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
                        val x = i * it.max().x + (1 - i) * it.min().x
                        val y = j * it.max().y + (1 - j) * it.min().y
                        val z = k * it.max().z + (1 - k) * it.min().z

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
            aabb(min, max)
        }
    }

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
        val rec = delegate.hit(rotatedRay, tMin, tMax) ?: return null
        val p = Point3(
            cosTheta * rec.p.x + sinTheta * rec.p.z,
            rec.p.y,
            -sinTheta * rec.p.x + cosTheta * rec.p.z
        )
        val normal = Vector3(
            cosTheta * rec.normal.x + sinTheta * rec.normal.z,
            rec.normal.y,
            -sinTheta * rec.normal.x + cosTheta * rec.normal.z
        )
        rec.p = p
        rec.setFaceNormal(rotatedRay, normal)
        return rec
    }

    override fun boundingBox(time0: Double, time1: Double): aabb? {
        return bbox
    }
}

private class FlipFace(private val delegate: Hittable) : Hittable() {

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        val rec = delegate.hit(r, tMin, tMax) ?: return null
        rec.frontFace = !rec.frontFace
        return rec
    }

    override fun boundingBox(time0: Double, time1: Double): aabb? {
        return delegate.boundingBox(time0, time1)
    }
}
