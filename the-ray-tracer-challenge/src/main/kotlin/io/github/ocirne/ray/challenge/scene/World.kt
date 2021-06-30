package io.github.ocirne.ray.challenge.scene

import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.raysphere.Computation
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.tuples.BLACK
import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point

class World(val shapes: List<Shape> = listOf(), val lights: List<PointLight> = listOf()) {

    fun intersect(ray: Ray): List<Intersection> {
        return shapes.map { o -> o.intersect(ray) }
            .filter { hit -> hit.isNotEmpty() }
            .flatten()
            .filter { hit -> hit.t >= 0}
            .sortedBy { s -> s.t }
    }

    fun addLight(light: PointLight): World {
        return World(shapes, lights + light)
    }

    fun withLight(light: PointLight): World {
        return World(shapes, listOf(light))
    }

    fun shadeHit(comps: Computation): Color {
        val shadowed = isShadowed(comps.overPoint)

        return lights.map { light ->
            comps.obj.material.lighting(light, comps.overPoint, comps.eyeV, comps.normalV, shadowed)
        }.reduce { acc, color -> acc + color }
    }

    fun colorAt(ray: Ray): Color {
        val hits = intersect(ray)
        if (hits.isEmpty()) {
            return BLACK
        }
        val comps = hits.first().prepareComputations(ray)
        return shadeHit(comps)
    }

    fun isShadowed(point: Point): Boolean {
        // TODO was machen bei mehreren Lichtern?
        val light = lights.first()
        val v = light.position - point
        val distance = v.magnitude()
        val direction = v.normalize()
        val r = Ray(point, direction)
        val intersections = intersect(r)
        return intersections.isNotEmpty() && intersections.first().t < distance
    }
}
