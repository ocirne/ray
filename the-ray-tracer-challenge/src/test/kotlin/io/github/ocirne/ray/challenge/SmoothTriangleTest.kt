package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.math.equalsDelta
import io.github.ocirne.ray.challenge.raysphere.Intersections
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.triangles.SmoothTriangle
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SmoothTriangleTest {

    val p1 = point(0, 1, 0)
    val p2 = point(-1, 0, 0)
    val p3 = point(1, 0, 0)
    val n1 = vector(0, 1, 0)
    val n2 = vector(-1, 0, 0)
    val n3 = vector(1, 0, 0)
    val tri = SmoothTriangle(p1, p2, p3, n1, n2, n3)

    @Test
    fun `Scenario Constructing a smooth triangle`() {
        tri.p1 shouldBe p1
        tri.p2 shouldBe p2
        tri.p3 shouldBe p3
        tri.n1 shouldBe n1
        tri.n2 shouldBe n2
        tri.n3 shouldBe n3
    }

    @Test
    fun `Scenario An intersection with a smooth triangle stores u v`() {
        val r = Ray(point(-0.2, 0.3, -2.0), vector(0, 0, 1))
        val xs = tri.localIntersect(r)
        xs[0].u!!.equalsDelta(0.45) shouldBe true
        xs[0].v!!.equalsDelta(0.25) shouldBe true
    }

    @Test
    fun `Scenario A smooth triangle uses u v to interpolate the normal`() {
        val i = tri.intersectionWithUV(1.0, 0.45, 0.25)
        val n = tri.normalAt(point(0, 0, 0), i)
        n shouldBe vector(-0.5547, 0.83205, 0.0)
    }

    @Test
    fun `Scenario Preparing the normal on a smooth triangle`() {
        val i = tri.intersectionWithUV(1.0, 0.45, 0.25)
        val r = Ray(point(-0.2, 0.3, -2.0), vector(0, 0, 1))
//  TODO (häh?)      val xs = Intersections(i)
        val comps = i.prepareComputations(r, listOf(i))
        comps.normalV shouldBe vector(-0.5547, 0.83205, 0.0)
    }
}