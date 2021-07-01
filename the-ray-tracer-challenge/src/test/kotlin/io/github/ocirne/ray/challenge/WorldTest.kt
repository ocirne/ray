package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

fun defaultWorld(): World {
    val  light = PointLight (point(-10, 10, -10), color(1, 1, 1))
    val  s1 = Sphere(material = Material(
        color = Color(0.8, 1.0, 0.6),
        diffuse = 0.7,
        specular = 0.2))
    val s2 = Sphere(transform = scaling(0.5, 0.5, 0.5))
    return World (shapes = listOf(s1, s2), lights = listOf(light))
}

internal class WorldTest {

  @Test
  fun `Scenario Creating a world`() {
    val  w = World()
    w.shapes shouldHaveSize  0
    w.lights shouldHaveSize  0
  }

  @Test
  fun `Scenario The default world`() {
      val  light = PointLight (point(-10, 10, -10), color(1, 1, 1))
      val  s1 = Sphere(material = Material(
          color = Color(0.8, 1.0, 0.6),
          diffuse = 0.7,
          specular = 0.2))
      val s2 = Sphere(transform = scaling(0.5, 0.5, 0.5))
      val w = defaultWorld()
      w.lights shouldContain light
      w.shapes shouldContain s1
      w.shapes shouldContain s2
   }

            @Test
            fun `Scenario Intersect a world with a ray`() {
    val w = defaultWorld ()
    val  r = Ray (point(0, 0, -5), vector(0, 0, 1))
    val xs = w.intersect (r)
    xs . size shouldBe 4
      xs [0].t shouldBe 4
      xs [1].t shouldBe 4.5
      xs [2].t shouldBe 5.5
      xs [3].t shouldBe 6
  }

    @Test
    fun `Scenario Shading an intersection`() {
    val w = defaultWorld ()
    val  r = Ray (point(0, 0, -5), vector(0, 0, 1))
    val  shape = w.shapes.first()
    val  i = Intersection (4, shape)
    val comps = i.prepareComputations (r)
    val  c = w.shadeHit(comps)
    c shouldBe color (0.38066, 0.47583, 0.2855)
  }

    @Test
    fun `Scenario Shading an intersection from the inside`() {
    val w = defaultWorld ().withLight(PointLight(point(0.0, 0.25, 0.0), color(1, 1, 1)))
    val  r = Ray (point(0, 0, 0), vector(0, 0, 1))
    val  shape = w.shapes.get(1)
    val  i = Intersection(0.5, shape)
    val comps = i.prepareComputations (r)
    val  c = w.shadeHit( comps)
    c shouldBe color (0.90498, 0.90498, 0.90498)
  }

    @Test
    fun `Scenario The color when a ray misses`() {
    val w = defaultWorld ()
    val  r = Ray (point(0, 0, -5), vector(0, 1, 0))
    val c = w.colorAt (r)
    c shouldBe color (0, 0, 0)
  }

    @Test
    fun `Scenario The color when a ray hits`() {
    val w = defaultWorld ()
    val  r = Ray (point(0, 0, -5), vector(0, 0, 1))
    val c = w.colorAt (r)
    c shouldBe color (0.38066, 0.47583, 0.2855)
  }

    @Test
    fun `Scenario The color with an intersection behind the ray`() {
        val w = defaultWorld()
        val outer = w.shapes.first()
        outer.material.ambient = 1.0
        val inner = w.shapes.get(1)
        inner.material.ambient = 1.0
        val r = Ray(point(0.0, 0.0, 0.75), vector(0, 0, -1))
        val c = w.colorAt(r)
        c shouldBe inner.material.color
    }

            @Test
            fun `Scenario There is no shadow when nothing is collinear with point and light`() {
            val w = defaultWorld ()
    val  p = point (0, 10, 0)
    w.isShadowed (p) shouldBe false
  }

    @Test
    fun `Scenario The shadow when an object is between the point and the light`() {
    val w = defaultWorld ()
    val  p = point (10, -10, 10)
    w.isShadowed (p) shouldBe true
  }

    @Test
    fun `Scenario There is no shadow when an object is behind the light`() {
    val w = defaultWorld ()
    val  p = point (-20, 20, -20)
    w.isShadowed (p) shouldBe false
  }

    @Test
    fun `Scenario There is no shadow when an object is behind the point`() {
    val w = defaultWorld ()
    val  p = point (-2, 2, -2)
    w.isShadowed (p) shouldBe false
  }

    @Test
    fun `Scenario shadeHit() is given an intersection in shadow`() {
        val s1 = Sphere ()
        val s2 = Sphere (transform = translation(0, 0, 10))
        val light = PointLight (point(0, 0, -10), color(1, 1, 1))
        val w = World (listOf(s1, s2), listOf(light))
        val  r = Ray (point(0, 0, 5), vector(0, 0, 1))
        val  i = Intersection(4, s2)
        val comps = i.prepareComputations (r)
        val  c = w.shadeHit (comps)
        c shouldBe color (0.1, 0.1, 0.1)
  }
/*
    @Test
    fun `Scenario The reflected color for a nonreflective material`() {
    val w = defaultWorld ()
    val  r = Ray (point(0, 0, 0), vector(0, 0, 1))
    val  shape = the second object in w
    val  shape . material . ambient = 1
    val  i = Intersection(1, shape)
    val comps = prepareComputations (i, r)
    val  color = reflected_color (w, comps)
    color shouldBe color (0, 0, 0)
  }

    @Test
    fun `Scenario The reflected color for a reflective material`() {
    val w = defaultWorld ()
    val  shape = Plane () with :
    | material.reflective | 0.5                   |
    | transform           | translation(0, -1, 0) |
    val  shape is added to w
            val  r = Ray (point(0, 0, -3), vector(0, -√2/2, √2/2))
    val  i = Intersection(√2, shape)
    val comps = prepareComputations (i, r)
    val  color = reflected_color (w, comps)
    color shouldBe color (0.19032, 0.2379, 0.14274)
  }

    @Test
    fun `Scenario shadeHit() with a reflective material`() {
    val w = defaultWorld ()
    val  shape = Plane () with :
    | material.reflective | 0.5                   |
    | transform           | translation(0, -1, 0) |
    val  shape is added to w
            val  r = Ray (point(0, 0, -3), vector(0, -√2/2, √2/2))
    val  i = Intersection(√2, shape)
    val comps = prepareComputations (i, r)
    val  color = shadeHit (w, comps)
    color shouldBe color (0.87677, 0.92436, 0.82918)
  }

    @Test
    fun `Scenario colorAt() with mutually reflective surfaces`() {
    val w = world ()
    val  w . light = PointLight (point(0, 0, 0), color(1, 1, 1))
    val  lower = Plane () with :
    | material.reflective | 1                     |
    | transform           | translation(0, -1, 0) |
    val  lower is added to w
            val  upper = Plane () with :
    | material.reflective | 1                    |
    | transform           | translation(0, 1, 0) |
    val  upper is added to w
            val  r = Ray (point(0, 0, 0), vector(0, 1, 0))
    colorAt (w, r) should terminate successfully
  }

    @Test
    fun `Scenario The reflected color at the maximum recursive depth`() {
    val w = defaultWorld ()
    val  shape = Plane () with :
    | material.reflective | 0.5                   |
    | transform           | translation(0, -1, 0) |
    val  shape is added to w
            val  r = Ray (point(0, 0, -3), vector(0, -√2/2, √2/2))
    val  i = Intersection(√2, shape)
    val comps = prepareComputations (i, r)
    val  color = reflected_color (w, comps, 0)
    color shouldBe color (0, 0, 0)
  }

    @Test
    fun `Scenario The refracted color with an opaque surface`() {
    val w = defaultWorld ()
    val  shape = the first object in w
    val  r = Ray (point(0, 0, -5), vector(0, 0, 1))
    val  xs = intersections (4:shape, 6:shape)
    val comps = prepareComputations (xs[0], r, xs)
    val  c = refracted_color (w, comps, 5)
    c shouldBe color (0, 0, 0)
  }

    @Test
    fun `Scenario The refracted color at the maximum recursive depth`() {
    val w = defaultWorld ()
    val  shape = the first object in w
    val  shape has:
    | material.transparency     | 1.0 |
    | material.refractive_index | 1.5 |
    val  r = Ray (point(0, 0, -5), vector(0, 0, 1))
    val  xs = intersections (4:shape, 6:shape)
    val comps = prepareComputations (xs[0], r, xs)
    val  c = refracted_color (w, comps, 0)
    c shouldBe color (0, 0, 0)
  }

    @Test
    fun `Scenario The refracted color under total internal reflection`() {
    val w = defaultWorld ()
    val  shape = the first object in w
    val  shape has:
    | material.transparency     | 1.0 |
    | material.refractive_index | 1.5 |
    val  r = Ray (point(0, 0, √2/2), vector(0, 1, 0))
    val  xs = intersections (-√2/2:shape, √2/2:shape)
    # NOTE: this time you're inside the sphere, so you need
    # to look at the second intersection, xs[1], not xs[0]
    val comps = prepareComputations (xs[1], r, xs)
    val  c = refracted_color (w, comps, 5)
    c shouldBe color (0, 0, 0)
  }

    @Test
    fun `Scenario The refracted color with a refracted ray`() {
    val w = defaultWorld ()
    val  A = the first object in w
    val  A has:
    | material.ambient | 1.0            |
    | material.pattern | TestPattern() |
    val  B = the second object in w
    val  B has:
    | material.transparency     | 1.0 |
    | material.refractive_index | 1.5 |
    val  r = Ray (point(0, 0, 0.1), vector(0, 1, 0))
    val  xs = intersections (-0.9899:A, -0.4899:B, 0.4899:B, 0.9899:A)
    val comps = prepareComputations (xs[2], r, xs)
    val  c = refracted_color (w, comps, 5)
    c shouldBe color (0, 0.99888, 0.04725)
  }

    @Test
    fun `Scenario shadeHit() with a transparent material`() {
    val w = defaultWorld ()
    val  floor = Plane () with :
    | transform                 | translation(0, -1, 0) |
    | material.transparency     | 0.5                   |
    | material.refractive_index | 1.5                   |
    val  floor is added to w
            val  ball = Sphere () with :
    | material.color     | (1, 0, 0)                  |
    | material.ambient   | 0.5                        |
    | transform          | translation(0, -3.5, -0.5) |
    val  ball is added to w
            val  r = Ray (point(0, 0, -3), vector(0, -√2/2, √2/2))
    val  xs = intersections (√2:floor)
    val comps = prepareComputations (xs[0], r, xs)
    val  color = shadeHit (w, comps, 5)
    color shouldBe color (0.93642, 0.68642, 0.68642)
  }

    @Test
    fun `Scenario shadeHit() with a reflective, transparent material`() {
    val w = defaultWorld ()
    val  r = Ray (point(0, 0, -3), vector(0, -√2/2, √2/2))
    val  floor = Plane () with :
    | transform                 | translation(0, -1, 0) |
    | material.reflective       | 0.5                   |
    | material.transparency     | 0.5                   |
    | material.refractive_index | 1.5                   |
    val  floor is added to w
            val  ball = Sphere () with :
    | material.color     | (1, 0, 0)                  |
    | material.ambient   | 0.5                        |
    | transform          | translation(0, -3.5, -0.5) |
    val  ball is added to w
            val  xs = intersections (√2:floor)
    val comps = prepareComputations (xs[0], r, xs)
    val  color = shadeHit (w, comps, 5)
    color shouldBe color (0.93391, 0.69643, 0.69243)
  }
      */
}