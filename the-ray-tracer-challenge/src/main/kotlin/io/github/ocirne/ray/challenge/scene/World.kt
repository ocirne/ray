package io.github.ocirne.ray.challenge.scene

import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.raysphere.Computation
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.raysphere.Sphere
import io.github.ocirne.ray.challenge.tuples.BLACK
import io.github.ocirne.ray.challenge.tuples.Color

class World(val objects: List<Sphere> = listOf(), val lights: List<PointLight> = listOf()) {

    fun intersect(ray: Ray): List<Intersection> {
        return objects.map { o -> o.intersect(ray) }
            .filter { hit -> hit.isNotEmpty() }
            .flatten()
            .sortedBy { s -> s.t }
    }

    fun addLight(light: PointLight): World {
        return World(objects, lights + light)
    }

    fun withLight(light: PointLight): World {
        return World(objects, listOf(light))
    }

    fun shadeHit(comps: Computation): Color {
        println("lights: " + lights)
        return lights.map { light ->
            comps.obj.material.lighting(light, comps.point, comps.eyev, comps.normalv)
        }.reduce { acc, color -> acc + color }
    }

    fun colorAt(ray: Ray): Color {
        return BLACK
    }
}
