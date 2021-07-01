package io.github.ocirne.ray.challenge.lights

import io.github.ocirne.ray.challenge.patterns.Pattern
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.pow

data class Material(
    val color: Color = WHITE,
    val pattern: Pattern? = null,
    // TODO val?
    var ambient: Double = 0.1,
    val diffuse: Double = 0.9,
    val specular: Double = 0.9,
    val shininess: Double = 200.0,
    val reflective: Double = 0.0,
    val transparency: Double = 0.0,
    val refractiveIndex: Double = 1.0
) {
    fun lighting(shape: Shape, light: PointLight, point: Point, eyev: Vector, normalv: Vector, inShadow: Boolean=false): Color {
        val patternColor = pattern?.patternAtShape(shape, point) ?: color
        val effectiveColor = patternColor * light.intensity
        val lightv = (light.position - point).normalize()
        val effectiveAmbient = effectiveColor * ambient
        if (inShadow) {
            return effectiveAmbient
        }
        val lightDotNormal = lightv.dot(normalv)
        var effectiveDiffuse = BLACK
        var effectiveSpecular = BLACK
        if (lightDotNormal >= 0) {
            effectiveDiffuse = effectiveColor * diffuse * lightDotNormal
            val reflectV = (-lightv).reflect(normalv)
            val reflectDotEye = reflectV.dot(eyev)
            if (reflectDotEye > 0) {
                val factor = reflectDotEye.pow(shininess)
                effectiveSpecular = light.intensity * specular * factor
            }
        }
        return effectiveAmbient + effectiveDiffuse + effectiveSpecular
    }
}
