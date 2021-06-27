package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.material.Material
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Ray

class Box(private val p0: Point3,
          private val p1: Point3,
          material: Material) : Hittable() {

    private val sides: HittableList

    init {
        val objects = HittableList.Builder()
        objects.add(XYRect(p0.x, p1.x, p0.y, p1.y, p1.z, material))
        objects.add(XYRect(p0.x, p1.x, p0.y, p1.y, p0.z, material))
        objects.add(XZRect(p0.x, p1.x, p0.z, p1.z, p1.y, material))
        objects.add(XZRect(p0.x, p1.x, p0.z, p1.z, p0.y, material))
        objects.add(YZRect(p0.y, p1.y, p0.z, p1.z, p1.x, material))
        objects.add(YZRect(p0.y, p1.y, p0.z, p1.z, p0.x, material))
        sides = objects.build()
    }

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        return sides.hit(r, tMin, tMax)
    }

    override fun boundingBox(time0: Double, time1: Double): AABB {
        return AABB(p0, p1)
    }
}
