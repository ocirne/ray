package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.random.Random

class bvh_node(src_objects: hittable_list, start: Int=0, end: Int=src_objects.objects.size, time0: Double, time1: Double): hittable {

    var left: hittable
    var right: hittable
    var box: aabb

    override fun hit(r: Ray, t_min: Double, t_max: Double): hit_record? {
        if (!box.hit(r, t_min, t_max)) {
            return null
        }
        val hit_left = left.hit(r, t_min, t_max)
        val hit_right = right.hit(r, t_min, hit_left?.t ?: t_max)

        hit_left?. let { return hit_left }
        return hit_right
    }

    override fun bounding_box(time0: Double, time1: Double): aabb {
        return box
    }

    init {
        val objects = src_objects // Create a modifiable array of the source scene objects

        val axis = Random.nextInt(0, 3)
        val comparator: Comparator<hittable> = when (axis) {
            0 -> box_x_compare()
            1 -> box_y_compare()
            else -> box_z_compare()
        }

        val object_span = end - start

        if (object_span == 1) {
            left = objects[start]
            right = objects[start]
        } else if (object_span == 2) {
            val cmp = comparator.compare(objects[start], objects[start + 1])
            if (cmp > 0) {
                left = objects[start]
                right = objects[start + 1]
            } else {
                left = objects[start + 1]
                right = objects[start]
            }
        } else {
            objects.objects.sortWith(comparator, start, end)

            val mid = start + object_span / 2
            left = bvh_node(objects, start, mid, time0, time1)
            right = bvh_node(objects, mid, end, time0, time1)
        }

        val box_left = left.bounding_box(time0, time1)
        val box_right = right.bounding_box(time0, time1)

        if (box_left == null || box_right == null) {
            System.err.println("No bounding box in bvh_node constructor.")
        }
        box = surrounding_box(box_left!!, box_right!!)
    }
}

open class box_compare(val axis: Int) : Comparator<hittable> {

    override fun compare(a: hittable, b: hittable): Int {
        val box_a = a.bounding_box(0.0, 0.0)
        val box_b = b.bounding_box(0.0, 0.0)

        if (box_a == null || box_b == null) {
            System.err.println("No bounding box in bvh_node constructor.")
        }
        when (axis) {
            (0) -> return box_a!!.min().x.compareTo(box_b!!.min().x)
            (1) -> return box_a!!.min().y.compareTo(box_b!!.min().y)
            (2) -> return box_a!!.min().z.compareTo(box_b!!.min().z)
            else -> throw IllegalArgumentException()
        }
    }
}

class box_x_compare : box_compare(0)

class box_y_compare : box_compare(1)

class box_z_compare : box_compare(2)
