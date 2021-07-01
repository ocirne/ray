package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.patterns.StripePattern
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

internal class MaterialTest {

    private val magic2 = sqrt(2.0) / 2.0

    private val m = Material()
    private val position = point(0, 0, 0)

    @Test
    fun `Scenario The default material`() {
        val m = Material()
        m.color shouldBe color(1, 1, 1)
        m.ambient shouldBe 0.1
        m.diffuse shouldBe 0.9
        m.specular shouldBe 0.9
        m.shininess shouldBe 200.0
    }

    @Test
    fun `Scenario Lighting with the eye between the light and the surface`() {
        val eyev = vector(0, 0, -1)
        val normalv = vector(0, 0, -1)
        val light = PointLight(point(0, 0, -10), color(1, 1, 1))
        val result = m.lighting(light, position, eyev, normalv)
        result shouldBe color(1.9, 1.9, 1.9)
    }

    @Test
    fun `Scenario Lighting with the eye between light and surface, eye offset 45 deg`() {
        val eyev = vector(0.0, magic2, magic2)
        val normalv = vector(0, 0, -1)
        val light = PointLight(point(0, 0, -10), color(1, 1, 1))
        val result = m.lighting(light, position, eyev, normalv)
        result shouldBe color(1.0, 1.0, 1.0)
    }

    @Test
    fun `Scenario Lighting with eye opposite surface, light offset 45 deg`() {
        val eyev = vector(0, 0, -1)
        val normalv = vector(0, 0, -1)
        val light = PointLight(point(0, 10, -10), color(1, 1, 1))
        val result = m.lighting(light, position, eyev, normalv)
        result shouldBe color(0.7364, 0.7364, 0.7364)
    }

    @Test
    fun `Scenario Lighting with eye in the path of the reflection vector`() {
        val eyev = vector(0.0, -magic2, -magic2)
        val normalv = vector(0, 0, -1)
        val light = PointLight(point(0, 10, -10), color(1, 1, 1))
        val result = m.lighting(light, position, eyev, normalv)
        result shouldBe color(1.6364, 1.6364, 1.6364)
    }

    @Test
    fun `Scenario Lighting with the light behind the surface`() {
        val eyev = vector(0, 0, -1)
        val normalv = vector(0, 0, -1)
        val light = PointLight(point(0, 0, 10), color(1, 1, 1))
        val result = m.lighting(light, position, eyev, normalv)
        result shouldBe color(0.1, 0.1, 0.1)
    }

      @Test
      fun `Scenario Lighting with the surface in shadow`() {
        val eyeV = vector (0, 0, -1)
        val normalV = vector (0, 0, -1)
        val light = PointLight(point(0, 0, -10), color(1, 1, 1))
        val inShadow = true
        val result = m.lighting(light, position, eyeV, normalV, inShadow)
        result shouldBe color (0.1, 0.1, 0.1)
    }

        @Test
        fun `Scenario Lighting with a pattern applied`() {
          val m = Material(
              pattern = StripePattern (WHITE, BLACK),
              ambient = 1.0,
              diffuse = 0.0,
              specular = 0.0)
          val eyev = vector (0, 0, -1)
          val normalv = vector (0, 0, -1)
          val light = PointLight (point(0, 0, -10), color(1, 1, 1))
          val c1 = m.lighting (light, point(0.9, 0.0, 0.0), eyev, normalv, false)
          val c2 = m.lighting (light, point(1.1, 0.0, 0.0), eyev, normalv, false)
          c1 shouldBe color (1, 1, 1)
          c2 shouldBe color (0, 0, 0)
        }
/*
      @Test
      fun `Scenario Reflectivity for the default material`() {
  val m = Material()
  m.reflective shouldBe 0.0
}

      @Test
      fun `Scenario Transparency and Refractive Index for the default material`() {
  val m = Material()
  m.transparency shouldBe 0.0
    val m.refractive_index shouldBe 1.0
}

 */
      }
