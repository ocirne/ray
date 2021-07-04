package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.point

class Bounds(val minimum: Point, val maximum: Point) {

    fun corners(): List<Point> {
        return listOf(
            point(minimum.x, minimum.y, minimum.z),
            point(minimum.x, minimum.y, maximum.z),
            point(minimum.x, maximum.y, minimum.z),
            point(minimum.x, maximum.y, maximum.z),
            point(maximum.x, minimum.y, minimum.z),
            point(maximum.x, minimum.y, maximum.z),
            point(maximum.x, maximum.y, minimum.z),
            point(maximum.x, maximum.y, maximum.z)
        )
    }
}