package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.*
import io.github.ocirne.ray.challenge.shapes.Operation.*
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Constructive Solid Geometry */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CSGTest {

    @Test
    fun `Scenario CSG is created with an operation and two shapes`() {
        val s1 = Sphere()
        val s2 = Cube()
        val c = CSG(UNION, s1, s2)
        c.operation shouldBe UNION
        c.left shouldBe s1
        c.right shouldBe s2
        s1.parent shouldBe c
        s2.parent shouldBe c
    }

    data class ExampleCSGRules(
        val op: Operation,
        val lhit: Boolean,
        val inl: Boolean,
        val inr: Boolean,
        val result: Boolean
    )

    fun examplesCSGRules() = listOf(
        ExampleCSGRules(UNION, true, true, true, false),
        ExampleCSGRules(UNION, true, true, false, true),
        ExampleCSGRules(UNION, true, false, true, false),
        ExampleCSGRules(UNION, true, false, false, true),
        ExampleCSGRules(UNION, false, true, true, false),
        ExampleCSGRules(UNION, false, true, false, false),
        ExampleCSGRules(UNION, false, false, true, true),
        ExampleCSGRules(UNION, false, false, false, true),
        ExampleCSGRules(INTERSECTION, true, true, true, true),
        ExampleCSGRules(INTERSECTION, true, true, false, false),
        ExampleCSGRules(INTERSECTION, true, false, true, true),
        ExampleCSGRules(INTERSECTION, true, false, false, false),
        ExampleCSGRules(INTERSECTION, false, true, true, true),
        ExampleCSGRules(INTERSECTION, false, true, false, true),
        ExampleCSGRules(INTERSECTION, false, false, true, false),
        ExampleCSGRules(INTERSECTION, false, false, false, false),
        ExampleCSGRules(DIFFERENCE, true, true, true, false),
        ExampleCSGRules(DIFFERENCE, true, true, false, true),
        ExampleCSGRules(DIFFERENCE, true, false, true, false),
        ExampleCSGRules(DIFFERENCE, true, false, false, true),
        ExampleCSGRules(DIFFERENCE, false, true, true, true),
        ExampleCSGRules(DIFFERENCE, false, true, false, true),
        ExampleCSGRules(DIFFERENCE, false, false, true, false),
        ExampleCSGRules(DIFFERENCE, false, false, false, false)
    )

    @ParameterizedTest
    @MethodSource("examplesCSGRules")
    fun `Scenario Outline Evaluating the rule for a CSG operation`(ex: ExampleCSGRules) {
        val result = intersectionAllowed(ex.op, ex.lhit, ex.inl, ex.inr)
        result shouldBe ex.result
    }

    data class ExampleFilteringIntersections(val operation: Operation, val x0: Int, val x1: Int)

    fun examplesFilteringIntersections() = listOf(
        ExampleFilteringIntersections(UNION, 0, 3),
        ExampleFilteringIntersections(INTERSECTION, 1, 2),
        ExampleFilteringIntersections(DIFFERENCE, 0, 1)
    )

    @ParameterizedTest
    @MethodSource("examplesFilteringIntersections")
    fun `Scenario Outline Filtering a list of intersections`(ex: ExampleFilteringIntersections) {
        val s1 = Sphere()
        val s2 = Cube()
        val c = CSG(ex.operation, s1, s2)
        val xs = listOf(Intersection(1, s1), Intersection(2, s2), Intersection(3, s1), Intersection(4, s2))
        val result = c.filterIntersections(xs)
        result.size shouldBe 2
        result[0] shouldBe xs[ex.x0]
        result[1] shouldBe xs[ex.x1]
    }

    @Test
    fun `Scenario A ray misses a CSG object`() {
        val c = CSG(UNION, Sphere(), Cube())
        val r = Ray(point(0, 2, -5), vector(0, 0, 1))
        val xs = c.localIntersect(r)
        xs should beEmpty()
    }

    @Test
    fun `Scenario A ray hits a CSG object`() {
        val s1 = Sphere()
        val s2 = Sphere(translation(0.0, 0.0, 0.5))
        val c = CSG(UNION, s1, s2)
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val xs = c.localIntersect(r)
        xs.size shouldBe 2
        xs[0].t shouldBe 4
        xs[0].shape shouldBe s1
        xs[1].t shouldBe 6.5
        xs[1].shape shouldBe s2
    }
}