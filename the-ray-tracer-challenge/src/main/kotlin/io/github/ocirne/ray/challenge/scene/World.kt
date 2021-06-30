package io.github.ocirne.ray.challenge.scene

import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.raysphere.Computation
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.raysphere.Sphere
import io.github.ocirne.ray.challenge.tuples.BLACK
import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point

class World(val objects: List<Sphere> = listOf(), val lights: List<PointLight> = listOf()) {

    fun intersect(ray: Ray): List<Intersection> {
        return objects.map { o -> o.intersect(ray) }
            .filter { hit -> hit.isNotEmpty() }
            .flatten()
            .filter { hit -> hit.t >= 0}
            .sortedBy { s -> s.t }
    }

    fun addLight(light: PointLight): World {
        return World(objects, lights + light)
    }

    fun withLight(light: PointLight): World {
        return World(objects, listOf(light))
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
