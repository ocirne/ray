package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.triangles.Triangle
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class TriangleTest {

    @Test
    fun `Scenario Constructing a Triangle`() {
        val p1 = point(0, 1, 0)
        val p2 = point(-1, 0, 0)
        val p3 = point(1, 0, 0)
        val t = Triangle(p1, p2, p3)
        t.p1 shouldBe p1
        t.p2 shouldBe p2
        t.p3 shouldBe p3
        t.e1 shouldBe vector(-1, -1, 0)
        t.e2 shouldBe vector(1, -1, 0)
        t.normal shouldBe vector(0, 0, -1)
    }

    @Test
    fun `Scenario Intersecting a ray parallel to the Triangle`() {
        val t = Triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
        val r = Ray(point(0, -1, -2), vector(0, 1, 0))
        val xs = t.localIntersect(r)
        xs should beEmpty()
    }

    @Test
    fun `Scenario A ray misses the p1-p3 edge`() {
        val t = Triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
        val r = Ray(point(1, 1, -2), vector(0, 0, 1))
        val xs = t.localIntersect(r)
        xs should beEmpty()
    }

    @Test
    fun `Scenario A ray misses the p1-p2 edge`() {
        val t = Triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
        val r = Ray(point(-1, 1, -2), vector(0, 0, 1))
        val xs = t.localIntersect(r)
        xs should beEmpty()
    }

    @Test
    fun `Scenario A ray misses the p2-p3 edge`() {
        val t = Triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
        val r = Ray(point(0, -1, -2), vector(0, 0, 1))
        val xs = t.localIntersect(r)
        xs should beEmpty()
    }

    @Test
    fun `Scenario A ray strikes a Triangle`() {
        val t = Triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
        val r = Ray(point(0.0, 0.5, -2.0), vector(0, 0, 1))
        val xs = t.localIntersect(r)
        xs.size shouldBe 1
        xs[0].t shouldBe 2
    }

    @Test
    fun `Scenario Finding the normal on a Triangle`() {
        val t = Triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
        val n1 = t.localNormalAt(point(0.0, 0.5, 0.0))
        val n2 = t.localNormalAt(point(-0.5, 0.75, 0.0))
        val n3 = t.localNormalAt(point(0.5, 0.25, 0.0))
        n1 shouldBe t.normal
        n2 shouldBe t.normal
        n3 shouldBe t.normal
    }
}