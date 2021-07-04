package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.math.equalsDelta
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Cylinder
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.doubles.beNegativeInfinity
import io.kotest.matchers.doubles.bePositiveInfinity
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CylinderTest {

    data class ExampleRayMisses(val origin: Point, val direction: Vector)

    fun examplesRayMisses() = listOf(
        ExampleRayMisses(point(1, 0, 0), vector(0, 1, 0)),
        ExampleRayMisses(point(0, 0, 0), vector(0, 1, 0)),
        ExampleRayMisses(point(0, 0, -5), vector(1, 1, 1))
    )

    @ParameterizedTest
    @MethodSource("examplesRayMisses")
    fun `Scenario Outline A ray misses a Cylinder`(ex: ExampleRayMisses) {
        val cyl = Cylinder()
        val direction = ex.direction.normalize()
        val r = Ray(ex.origin, direction)
        val xs = cyl.localIntersect(r)
        xs should beEmpty()
    }

    data class ExampleRayStrikes(val origin: Point, val direction: Vector, val t0: Double, val t1: Double)

    fun examplesRayStrikes() = listOf(
        ExampleRayStrikes(point(1, 0, -5), vector(0, 0, 1), 5.0, 5.0),
        ExampleRayStrikes(point(0, 0, -5), vector(0, 0, 1), 4.0, 6.0),
        ExampleRayStrikes(point(0.5, 0.0, -5.0), vector(0.1, 1.0, 1.0), 6.80798, 7.08872)
    )

    @ParameterizedTest
    @MethodSource("examplesRayStrikes")
    fun `Scenario Outline A ray strikes a Cylinder`(ex: ExampleRayStrikes) {
        val cyl = Cylinder()
        val direction = ex.direction.normalize()
        val r = Ray(ex.origin, direction)
        val xs = cyl.localIntersect(r)
        xs.size shouldBe 2
        xs[0].t.equalsDelta(ex.t0) shouldBe true
        xs[1].t.equalsDelta(ex.t1) shouldBe true
    }

    data class ExampleNormalVector(val point: Point, val normal: Vector)

    fun examplesNormalVector() = listOf(
        ExampleNormalVector(point(1, 0, 0), vector(1, 0, 0)),
        ExampleNormalVector(point(0, 5, -1), vector(0, 0, -1)),
        ExampleNormalVector(point(0, -2, 1), vector(0, 0, 1)),
        ExampleNormalVector(point(-1, 1, 0), vector(-1, 0, 0))
    )

    @ParameterizedTest
    @MethodSource("examplesNormalVector")
    fun `Scenario Outline Normal vector on a Cylinder`(ex: ExampleNormalVector) {
        val cyl = Cylinder()
        val n = cyl.localNormalAt(ex.point)
        n shouldBe ex.normal
    }

    @Test
    fun `Scenario The default minimum and maximum for a Cylinder`() {
        val cyl = Cylinder()
        cyl.minimum should beNegativeInfinity()
        cyl.maximum should bePositiveInfinity()
    }

    data class ExampleIntersectingConstrained(val number: Int, val point: Point, val direction: Vector, val count: Int)

    fun examplesIntersectingConstrained() = listOf(
        ExampleIntersectingConstrained(1, point(0.0, 1.5, 0.0), vector(0.1, 1.0, 0.0), 0),
        ExampleIntersectingConstrained(2, point(0, 3, -5), vector(0, 0, 1), 0),
        ExampleIntersectingConstrained(3, point(0, 0, -5), vector(0, 0, 1), 0),
        ExampleIntersectingConstrained(4, point(0, 2, -5), vector(0, 0, 1), 0),
        ExampleIntersectingConstrained(5, point(0, 1, -5), vector(0, 0, 1), 0),
        ExampleIntersectingConstrained(6, point(0.0, 1.5, -2.0), vector(0, 0, 1), 2)
    )

    @ParameterizedTest
    @MethodSource("examplesIntersectingConstrained")
    fun `Scenario Outline Intersecting a constrained Cylinder`(ex: ExampleIntersectingConstrained) {
        val cyl = Cylinder(minimum = 1.0, maximum = 2.0)
        val direction = ex.direction.normalize()
        val r = Ray(ex.point, direction)
        val xs = cyl.localIntersect(r)
        xs.size shouldBe ex.count
    }

    @Test
    fun `Scenario The default closed value for a Cylinder`() {
        val cyl = Cylinder()
        cyl.closed shouldBe false
    }

    data class ExampleIntersectingCaps(val number: Int, val point: Point, val direction: Vector, val count: Int)

    fun examplesIntersectingCaps() = listOf(
        ExampleIntersectingCaps(1, point(0, 3, 0), vector(0, -1, 0), 2),
        ExampleIntersectingCaps(2, point(0, 3, -2), vector(0, -1, 2), 2),
        ExampleIntersectingCaps(3, point(0, 4, -2), vector(0, -1, 1), 2),
        ExampleIntersectingCaps(4, point(0, 0, -2), vector(0, 1, 2), 2),
        ExampleIntersectingCaps(5, point(0, -1, -2), vector(0, 1, 1), 2)
    )

    @ParameterizedTest
    @MethodSource("examplesIntersectingCaps")
    fun `Scenario Outline Intersecting the caps of a closed Cylinder`(ex: ExampleIntersectingCaps) {
        val cyl = Cylinder(minimum = 1.0, maximum = 2.0, closed = true)
        val direction = ex.direction.normalize()
        val r = Ray(ex.point, direction)
        val xs = cyl.localIntersect(r)
        xs.size shouldBe ex.count
    }

    data class ExampleNormalsOnCaps(val point: Point, val normal: Vector)

    fun examplesNormalsOnCaps() = listOf(
        ExampleNormalsOnCaps(point(0.0, 1.0, 0.0), vector(0, -1, 0)),
        ExampleNormalsOnCaps(point(0.5, 1.0, 0.0), vector(0, -1, 0)),
        ExampleNormalsOnCaps(point(0.0, 1.0, 0.5), vector(0, -1, 0)),
        ExampleNormalsOnCaps(point(0.0, 2.0, 0.0), vector(0, 1, 0)),
        ExampleNormalsOnCaps(point(0.5, 2.0, 0.0), vector(0, 1, 0)),
        ExampleNormalsOnCaps(point(0.0, 2.0, 0.5), vector(0, 1, 0))
    )

    @ParameterizedTest
    @MethodSource("examplesNormalsOnCaps")
    fun `Scenario Outline The normal vector on a Cylinder's end caps`(ex: ExampleNormalsOnCaps) {
        val cyl = Cylinder(minimum = 1.0, maximum = 2.0, closed = true)
        val n = cyl.localNormalAt(ex.point)
        n shouldBe ex.normal
    }
}