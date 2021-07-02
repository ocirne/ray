package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.math.equalsDelta
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Cone
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.sqrt

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ConeTest {

    data class ExampleIntersectingCone(val origin: Point, val direction: Vector, val t0: Double, val t1: Double)

    fun examplesIntersectingCone() = listOf(
        ExampleIntersectingCone(point(0, 0, -5), vector(0, 0, 1), 5.0, 5.0),
        ExampleIntersectingCone(point(0, 0, -5), vector(1, 1, 1), 8.66025, 8.66025),
        ExampleIntersectingCone(point(1, 1, -5), vector(-0.5, -1.0, 1.0), 4.55006, 49.44994)
    )

    @ParameterizedTest
    @MethodSource("examplesIntersectingCone")
    fun `Scenario Outline Intersecting a cone with a ray`(ex: ExampleIntersectingCone) {
        val shape = Cone()
        val direction = ex.direction.normalize()
        val r = Ray(ex.origin, direction)
        val xs = shape.localIntersect(r)
        xs.size shouldBe 2
        xs[0].t.equalsDelta(ex.t0) shouldBe true
        xs[1].t.equalsDelta(ex.t1) shouldBe true
    }

    @Test
    fun `Scenario Intersecting a cone with a ray parallel to one of its halves`() {
        val shape = Cone()
        val direction = vector(0, 1, 1).normalize()
        val r = Ray(point(0, 0, -1), direction)
        val xs = shape.localIntersect(r)
        xs.size shouldBe 1
        xs[0].t.equalsDelta(0.35355) shouldBe true
    }

    data class ExampleIntersectingConeCaps(val origin: Point, val direction: Vector, val count: Int)

    fun examplesIntersectingConeCaps() = listOf(
        ExampleIntersectingConeCaps(point(0, 0, -5), vector(0, 1, 0), 0),
        ExampleIntersectingConeCaps(point(0.0, 0.0, -0.25), vector(0, 1, 1), 2),
        ExampleIntersectingConeCaps(point(0.0, 0.0, -0.25), vector(0, 1, 0), 4)
    )

    @ParameterizedTest
    @MethodSource("examplesIntersectingConeCaps")
    fun `Scenario Outline Intersecting a cone's end caps`(ex: ExampleIntersectingConeCaps) {
        val shape = Cone(minimum = -0.5, maximum = 0.5, closed = true)
        val direction = ex.direction.normalize()
        val r = Ray(ex.origin, direction)
        val xs = shape.localIntersect(r)
        xs.size shouldBe ex.count
    }

    data class ExampleNormalOnACone(val point: Point, val normal: Vector)

    fun examplesNormalOnACone() = listOf(
        ExampleNormalOnACone(point(0, 0, 0), vector(0, 0, 0)),
        ExampleNormalOnACone(point(1, 1, 1), vector(1.0, -sqrt(2.0), 1.0)),
        ExampleNormalOnACone(point(-1, -1, 0), vector(-1, 1, 0))
    )

    @ParameterizedTest
    @MethodSource("examplesNormalOnACone")
    fun `Scenario Outline Computing the normal vector on a cone`(ex: ExampleNormalOnACone) {
        val shape = Cone()
        val n = shape.localNormalAt(ex.point)
        n shouldBe ex.normal
    }
}