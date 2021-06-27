package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.random.Random

class HittableList(val objects: Array<Hittable>): Hittable() {

    operator fun get(i: Int): Hittable {
        return objects[i]
    }

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        var result: HitRecord? = null
        var closestSoFar = tMax

        for (obj in objects) {
            val tempRec = obj.hit(r, tMin, closestSoFar)
            tempRec?.let {
                closestSoFar = tempRec.t
                result = tempRec
            }
        }
        return result
    }

    override fun boundingBox(time0: Double, time1: Double): AABB? {
        if (objects.isEmpty()) {
            return null
        }
        var tempBox : AABB?
        var outputBox : AABB? = null
        var firstBox = true
        for (obj in objects) {
            tempBox = obj.boundingBox(time0, time1)
            if (tempBox == null) {
                return null
            }
            outputBox = if (firstBox) tempBox else surroundingBox(outputBox!!, tempBox)
            firstBox = false
        }
        return outputBox
    }

    override fun pdfValue(origin: Point3, v: Vector3): Double {
        val weight = 1.0 / objects.size
        return objects.sumOf { obj -> weight * obj.pdfValue(origin, v) }
    }

    override fun random(origin: Vector3): Vector3 {
        val intSize = objects.size
        return objects[Random.nextInt(0, intSize)].random(origin)
    }

    class Builder {

        val objects: ArrayList<Hittable> = ArrayList()

        fun add(obj: Hittable): Builder {
            objects.add(obj)
            return this
        }

        fun build() : HittableList {
            return HittableList(objects.toTypedArray())
        }
    }
}
