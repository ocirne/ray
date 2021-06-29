package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MaterialTest {

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

      @Test
      fun `Scenario Lighting with the eye between the light and the surface`() {
  val eyev = vector(0, 0, -1)
    val normalv = vector(0, 0, -1)
    val light = point_light(point(0, 0, -10), color(1, 1, 1))
  val result = lighting(m, light, position, eyev, normalv)
  result shouldBe color(1.9, 1.9, 1.9)
}

      @Test
      fun `Scenario Lighting with the eye between light and surface, eye offset 45°`() {
  val eyev = vector(0, √2/2, -√2/2)
    val normalv = vector(0, 0, -1)
    val light = point_light(point(0, 0, -10), color(1, 1, 1))
  val result = lighting(m, light, position, eyev, normalv)
  result shouldBe color(1.0, 1.0, 1.0)
}

      @Test
      fun `Scenario Lighting with eye opposite surface, light offset 45°`() {
  val eyev = vector(0, 0, -1)
    val normalv = vector(0, 0, -1)
    val light = point_light(point(0, 10, -10), color(1, 1, 1))
  val result = lighting(m, light, position, eyev, normalv)
  result shouldBe color(0.7364, 0.7364, 0.7364)
}

      @Test
      fun `Scenario Lighting with eye in the path of the reflection vector`() {
  val eyev = vector(0, -√2/2, -√2/2)
    val normalv = vector(0, 0, -1)
    val light = point_light(point(0, 10, -10), color(1, 1, 1))
  val result = lighting(m, light, position, eyev, normalv)
  result shouldBe color(1.6364, 1.6364, 1.6364)
}

      @Test
      fun `Scenario Lighting with the light behind the surface`() {
  val eyev = vector(0, 0, -1)
    val normalv = vector(0, 0, -1)
    val light = point_light(point(0, 0, 10), color(1, 1, 1))
  val result = lighting(m, light, position, eyev, normalv)
  result shouldBe color(0.1, 0.1, 0.1)
}

      @Test
      fun `Scenario Lighting with the surface in shadow`() {
        val eyev = vector (0, 0, -1)
        val normalv = vector (0, 0, -1)
        val light = point_light (point(0, 0, -10), color(1, 1, 1))
        val in_shadow = true
        val result = lighting (m, light, position, eyev, normalv, in_shadow)
        result shouldBe color (0.1, 0.1, 0.1)
}

        @Test
        fun `Scenario Lighting with a pattern applied`() {
          val m . pattern = stripe_pattern (color(1, 1, 1), color(0, 0, 0))
          val m . ambient = 1
          val m . diffuse = 0
          val m . specular = 0
          val eyev = vector (0, 0, -1)
          val normalv = vector (0, 0, -1)
          val light = point_light (point(0, 0, -10), color(1, 1, 1))
          val c1 = lighting (m, light, point(0.9, 0, 0), eyev, normalv, false)
          val c2 = lighting (m, light, point(1.1, 0, 0), eyev, normalv, false)
          c1 shouldBe color (1, 1, 1)
          c2 shouldBe color (0, 0, 0)
        }

 */
      }
