package io.github.ocirne.ray.challenge.scene

import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.raysphere.Computation
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.tuples.BLACK
import io.github.ocirne.ray.challenge.tuples.Color
import io.github.ocirne.ray.challenge.tuples.Point
import kotlin.math.sqrt

class World(val shapes: List<Shape> = listOf(), val lights: List<PointLight> = listOf()) {

    fun intersect(ray: Ray): List<Intersection> {
        return shapes.map { o -> o.intersect(ray) }
            .filter { hit -> hit.isNotEmpty() }
            .flatten()
            .filter { hit -> hit.t >= 0 }
            .sortedBy { s -> s.t }
    }

    fun addLight(light: PointLight): World {
        return World(shapes, lights + light)
    }

    fun withLight(light: PointLight): World {
        return World(shapes, listOf(light))
    }

    fun shadeHit(comps: Computation, remaining: Int = 4): Color {
        val shadowed = isShadowed(comps.overPoint)

        return lights.map { light ->
            val surface =
                comps.shape.material.lighting(comps.shape, light, comps.overPoint, comps.eyeV, comps.normalV, shadowed)
            val reflected = reflectedColor(comps, remaining)
            val refracted = refractedColor(comps, remaining)
            val material = comps.shape.material
            if (material.reflective > 0 && material.transparency > 0) {
                val reflectance = comps.schlick()
                surface + reflected * reflectance + refracted * (1 - reflectance)
            } else {
                surface + reflected + refracted
            }
        }.reduce { acc, color -> acc + color }
    }

    fun colorAt(ray: Ray, remaining: Int = 4): Color {
        val hits = intersect(ray)
        if (hits.isEmpty()) {
            return BLACK
        }
        val comps = hits.first().prepareComputations(ray, hits)
        return shadeHit(comps, remaining)
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

    fun reflectedColor(comps: Computation, remaining: Int): Color {
        if (remaining <= 1) {
            return BLACK
        }
        if (comps.shape.material.reflective == 0.0) {
            return BLACK
        }
        val reflectRay = Ray(comps.overPoint, comps.reflectV)
        val color = colorAt(reflectRay, remaining - 1)
        return color * comps.shape.material.reflective
    }

    fun refractedColor(comps: Computation, remaining: Int): Color {
        if (remaining == 0) {
            return BLACK
        }
        // Find the ratio of first index of refraction to the second.
        // (Yup, this is inverted from the definition of Snell's Law.)
        val n_ratio = comps.n1 / comps.n2

        // cos(theta_i) is the same as the dot product of the two vectors
        val cos_i = comps.eyeV.dot(comps.normalV)

        // Find sin(theta_t)^2 via trigonometric identity
        val sin2_t = n_ratio * n_ratio * (1 - cos_i * cos_i)

        if (sin2_t > 1) {
            return BLACK
        }
        // Find cos(theta_t) via trigonometric identity
        val cos_t = sqrt(1.0 - sin2_t)

        // Compute the direction of the refracted ray
        val direction = comps.normalV * (n_ratio * cos_i - cos_t) - comps.eyeV * n_ratio

        // Create the refracted ray
        val refract_ray = Ray(comps.underPoint, direction)

        // Find the color of the refracted ray, making sure to multiply
        // by the transparency value to account for any opacity
        return colorAt(refract_ray, remaining - 1) * comps.shape.material.transparency
    }
}
