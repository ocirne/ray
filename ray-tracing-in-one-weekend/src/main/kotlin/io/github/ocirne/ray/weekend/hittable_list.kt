package io.github.ocirne.ray.weekend

class hittable_list(val objects: Array<hittable>): hittable {

    operator fun get(i: Int): hittable {
        return objects[i]
    }

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        var result: hit_record? = null
        var closest_so_far = t_max

        for (obj in objects) {
            val temp_rec = obj.hit(r, t_min, closest_so_far)
            if (temp_rec != null) {
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

    class builder() {

        val objects: ArrayList<hittable> = ArrayList()

        fun clear() { objects.clear() }

        fun add(obj: hittable) { objects.add(obj) }

        fun build() : hittable_list {
            return hittable_list(objects.toTypedArray())
        }
    }
}
