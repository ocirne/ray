package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Group
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot

import org.junit.jupiter.api.Test

internal class GroupTest {

    @Test
    fun `Scenario Creating a new group`() {
        val g = Group()
        g.transform shouldBe identityMatrix
        g should beEmpty()
    }

    @Test
    fun `Scenario Adding a child to a group`() {
        val g = Group()
        val s = TestShape()
        g.addChild(s)
        g shouldNot beEmpty()
        g shouldContain s
        s.parent shouldBe g
    }

    @Test
    fun `Scenario Intersecting a ray with an empty group`() {
        val g = Group()
        val r = Ray(point(0, 0, 0), vector(0, 0, 1))
        val xs = g.localIntersect(r)
        xs should beEmpty()
    }

    @Test
    fun `Scenario Intersecting a ray with a nonempty group`() {
        val g = Group()
        val s1 = Sphere()
        val s2 = Sphere(translation(0, 0, -3))
        val s3 = Sphere(translation(5, 0, 0))
        g.addChild(s1)
        g.addChild(s2)
        g.addChild(s3)
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val xs = g.localIntersect(r)
        xs.size shouldBe 4
        xs[0].shape shouldBe s2
        xs[1].shape shouldBe s2
        xs[2].shape shouldBe s1
        xs[3].shape shouldBe s1
    }

    @Test
    fun `Scenario Intersecting a transformed group`() {
        val g = Group(scaling(2, 2, 2))
        val s = Sphere(translation(5, 0, 0))
        g.addChild(s)
        val r = Ray(point(10, 0, -10), vector(0, 0, 1))
        val xs = g.intersect(r)
        xs.size shouldBe 2
    }
}