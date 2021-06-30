package io.github.ocirne.ray.challenge.lights

import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.pow

data class Material(
    val color: Color = WHITE,
    // TODO val?
    var ambient: Double = 0.1,
    val diffuse: Double = 0.9,
    val specular: Double = 0.9,
    val shininess: Double = 200.0
) {
    fun lighting(light: PointLight, point: Point, eyev: Vector, normalv: Vector): Color {
        val effectiveColor = color * light.intensity
        val lightv = (light.position - point).normalize()
        val effectiveAmbient = effectiveColor * ambient
        val lightDotNormal = lightv.dot(normalv)
        var effectiveDiffuse = BLACK
        var effectiveSpecular = BLACK
        if (lightDotNormal >= 0) {
            effectiveDiffuse = effectiveColor * diffuse * lightDotNormal
            val reflectv = (-lightv).reflect(normalv)
            val reflectDotEye = reflectv.dot(eyev)
            if (reflectDotEye > 0) {
                val factor = reflectDotEye.pow(shininess)
                effectiveSpecular = light.intensity * specular * factor
            }
        }
        return effectiveAmbient + effectiveDiffuse + effectiveSpecular
    }
}
