package io.github.ocirne.ray.challenge.raysphere

class Intersection(val t: Double, val obj: Sphere) {

    constructor(t: Int, obj: Sphere): this(t.toDouble(), obj)
}

class Intersections(vararg intersections: Intersection) {

    val count = intersections.size
    private val elements = intersections

    operator fun get(index: Int): Intersection {
        return elements[index]
    }

    fun hit(): Intersection? {
        return elements.filter { i -> i.t > 0 }.minByOrNull { it.t }
    }
}