package io.github.ocirne.ray.challenge.scene

import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.raysphere.Sphere

class World(val objects: List<Sphere> = listOf(), val lights: List<PointLight> = listOf()) {

    fun intersect(ray: Ray): List<Intersection> {
        return objects.map { o -> o.intersect(ray) }
            .filter { hit -> hit.isNotEmpty() }
            .flatten()
            .sortedBy { s -> s.t }
    }
}
