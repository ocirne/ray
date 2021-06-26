package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.random.Random

class hittable_list(val objects: Array<hittable>): hittable {

    operator fun get(i: Int): hittable {
        return objects[i]
    }

    override fun hit(r: Ray, t_min: Double, t_max: Double): HitRecord? {
        var result: HitRecord? = null
        var closest_so_far = t_max

        for (obj in objects) {
            val temp_rec = obj.hit(r, t_min, closest_so_far)
            temp_rec?.let {
                closest_so_far = temp_rec.t
                result = temp_rec
            }
        }
        return result
    }

    override fun bounding_box(time0: Double, time1: Double): aabb? {
        if (objects.isEmpty()) {
            return null
        }
        var temp_box : aabb?
        var output_box : aabb? = null
        var first_box = true
        for (obj in objects) {
            temp_box = obj.bounding_box(time0, time1)
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

        val objects: ArrayList<hittable> = ArrayList()

        fun clear() { objects.clear() }

        fun add(obj: hittable): builder {
            objects.add(obj)
            return this
        }

        fun build() : hittable_list {
            return hittable_list(objects.toTypedArray())
        }
    }
}
