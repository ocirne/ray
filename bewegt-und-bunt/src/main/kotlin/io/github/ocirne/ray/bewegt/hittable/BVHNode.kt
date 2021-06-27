package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.random.Random

class BVHNode(objects: HittableList, start: Int=0, end: Int=objects.objects.size, time0: Double, time1: Double):
    Hittable() {

    private var left: Hittable
    private var right: Hittable
    private var box: AABB

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        if (!box.hit(r, tMin, tMax)) {
            return null
        }
        val hitLeft = left.hit(r, tMin, tMax)
        val hitRight = right.hit(r, tMin, hitLeft?.t ?: tMax)

        hitLeft?. let { return hitLeft }
        return hitRight
    }

    override fun boundingBox(time0: Double, time1: Double): AABB {
        return box
    }

    init {
        val axis = Random.nextInt(0, 3)

        val comparator: Comparator<Hittable> = when (axis) {
            0 -> BoxCompareX()
            1 -> BoxCompareY()
            else -> BoxCompareZ()
        }

        val objectSpan = end - start

        if (objectSpan == 1) {
            left = objects[start]
            right = objects[start]
        } else if (objectSpan == 2) {
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

            val mid = start + objectSpan / 2
            left = BVHNode(objects, start, mid, time0, time1)
            right = BVHNode(objects, mid, end, time0, time1)
        }

        val boxLeft = left.boundingBox(time0, time1)
        val boxRight = right.boundingBox(time0, time1)

        if (boxLeft == null || boxRight == null) {
            System.err.println("No bounding box in bvh_node constructor.")
        }
        box = surroundingBox(boxLeft!!, boxRight!!)
    }
}

open class BoxCompare(private val axis: Int) : Comparator<Hittable> {

    override fun compare(a: Hittable, b: Hittable): Int {
        val boxA = a.boundingBox(0.0, 0.0)
        val boxB = b.boundingBox(0.0, 0.0)

        if (boxA == null || boxB == null) {
            System.err.println("No bounding box in bvh_node constructor.")
        }
        return when (axis) {
            (0) -> boxA!!.minimum.x.compareTo(boxB!!.minimum.x)
            (1) -> boxA!!.minimum.y.compareTo(boxB!!.minimum.y)
            (2) -> boxA!!.minimum.z.compareTo(boxB!!.minimum.z)
            else -> throw IllegalArgumentException()
        }
    }
}

class BoxCompareX : BoxCompare(0)

class BoxCompareY : BoxCompare(1)

class BoxCompareZ : BoxCompare(2)
