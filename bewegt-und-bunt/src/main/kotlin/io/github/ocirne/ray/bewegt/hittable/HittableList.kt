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
        var closest_so_far = tMax

        for (obj in objects) {
            val temp_rec = obj.hit(r, tMin, closest_so_far)
            temp_rec?.let {
                closest_so_far = temp_rec.t
                result = temp_rec
            }
        }
        return result
    }

    override fun boundingBox(time0: Double, time1: Double): AABB? {
        if (objects.isEmpty()) {
            return null
        }
        var temp_box : AABB?
        var output_box : AABB? = null
        var first_box = true
        for (obj in objects) {
            temp_box = obj.boundingBox(time0, time1)
            if (temp_box == null) {
                return null
            }
            output_box = if (first_box) temp_box else surroundingBox(output_box!!, temp_box)
            first_box = false
        }
        return output_box
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
