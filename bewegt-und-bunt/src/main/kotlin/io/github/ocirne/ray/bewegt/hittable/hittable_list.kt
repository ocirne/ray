package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.random.Random

class hittable_list(val objects: Array<Hittable>): Hittable() {

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

    override fun boundingBox(time0: Double, time1: Double): aabb? {
        if (objects.isEmpty()) {
            return null
        }
        var temp_box : aabb?
        var output_box : aabb? = null
        var first_box = true
        for (obj in objects) {
            temp_box = obj.boundingBox(time0, time1)
            if (temp_box == null) {
                return null
            }
            output_box = if (first_box) temp_box else surrounding_box(output_box!!, temp_box)
            first_box = false
        }
        return output_box
    }

    override fun pdfValue(origin: Point3, v: Vector3): Double {
        val weight = 1.0 / objects.size
        var sum = 0.0

        for (obj in objects) {
            sum += weight * obj.pdfValue(origin, v)
        }
        return sum
    }

    override fun random(origin: Vector3): Vector3 {
        val int_size = objects.size
        return objects[Random.nextInt(0, int_size)].random(origin)
    }

    class builder {

        val objects: ArrayList<Hittable> = ArrayList()

        fun clear() { objects.clear() }

        fun add(obj: Hittable): builder {
            objects.add(obj)
            return this
        }

        fun build() : hittable_list {
            return hittable_list(objects.toTypedArray())
        }
    }
}