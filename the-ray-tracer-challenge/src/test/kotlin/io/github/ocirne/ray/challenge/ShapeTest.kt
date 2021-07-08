package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Bounds
import io.github.ocirne.ray.challenge.shapes.Group
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.transformations.rotationY
import io.github.ocirne.ray.challenge.transformations.rotationZ
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector
import io.github.ocirne.ray.challenge.tuples.point
import io.github.ocirne.ray.challenge.tuples.vector
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import org.junit.jupiter.api.Test
import kotlin.math.PI

class TestShape(transform: Matrix = identityMatrix, material: Material = Material()) : Shape(transform, material) {

    lateinit var savedRay: Ray

    override fun localIntersect(localRay: Ray): List<Intersection> {
        savedRay = localRay
        return listOf()
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection?): Vector {
        return localPoint - point(0, 0, 0)
    }

    override fun bounds(): Bounds {
        TODO("Not implemented")
    }
}

internal class ShapeTest {

    @Test
    fun `Scenario The default transformation`() {
        val s = TestShape()
        s.transform shouldBe identityMatrix
    }

    @Test
    fun `Scenario Assigning a transformation`() {
        val s = TestShape(translation(2, 3, 4))
        s.transform shouldBe translation(2, 3, 4)
    }

    @Test
    fun `Scenario The default material`() {
        val s = TestShape()
        s.material shouldBe Material()

    }

    @Test
    fun `Scenario Assigning a material`() {
        val m = Material(ambient = 1.0)
        val s = TestShape(material = m)
        s.material shouldBe m
    }

    @Test
    fun `Scenario Intersecting a scaled shape with a ray`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = TestShape(transform = scaling(2, 2, 2))
        val xs = s.intersect(r)
        s.savedRay.origin shouldBe point(0.0, 0.0, -2.5)
        s.savedRay.direction shouldBe vector(0.0, 0.0, 0.5)

    }

    @Test
    fun `Scenario Intersecting a translated shape with a ray`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = TestShape(transform = translation(5, 0, 0))
        val xs = s.intersect(r)
        s.savedRay.origin shouldBe point(-5, 0, -5)
        s.savedRay.direction shouldBe vector(0, 0, 1)

    }

    @Test
    fun `Scenario Computing the normal on a translated shape`() {
        val s = TestShape(transform = translation(0, 1, 0))
        val n = s.normalAt(point(0.0, 1.70711, -0.70711))
        n shouldBe vector(0.0, 0.70711, -0.70711)

    }

    @Test
    fun `Scenario Computing the normal on a transformed shape`() {
        val s = TestShape(transform = scaling(1.0, 0.5, 1.0) * rotationZ(PI / 5))
        val n = s.normalAt(point(0.0, magic2, -magic2))
        n shouldBe vector(0.0, 0.97014, -0.24254)
    }

    @Test
    fun `Scenario A Sphere is a Shape`() {
        val s = Sphere()
        s should beInstanceOf<Shape>()
    }

    @Test
    fun `Scenario A shape has a parent attribute`() {
        val s = TestShape()
        s.parent should beNull()
    }

    @Test
    fun `Scenario Converting a point from world to object space`() {
        val g1 = Group(rotationY(PI / 2))
        val g2 = Group(scaling(2, 2, 2))
        g1.addChild(g2)
        val s = Sphere(translation(5, 0, 0))
        g2.addChild(s)
        val p = s.worldToObject(point(-2, 0, -10))
        p shouldBe point(0, 0, -1)
    }

    @Test
    fun `Scenario Converting a normal from object to world space`() {
        val g1 = Group(rotationY(PI / 2))
        val g2 = Group(scaling(1, 2, 3))
        g1.addChild(g2)
        val s = Sphere(translation(5, 0, 0))
        g2.addChild(s)
        val n = s.normalToWorld(vector(magic3, magic3, magic3))
        n shouldBe vector(0.2857, 0.4286, -0.8571)
    }

    @Test
    fun `Scenario Finding the normal on a child object`() {
        val g1 = Group(rotationY(PI / 2))
        val g2 = Group(scaling(1, 2, 3))
        g1.addChild(g2)
        val s = Sphere(translation(5, 0, 0))
        g2.addChild(s)
        val n = s.normalAt(point(1.7321, 1.1547, -5.5774))
        n shouldBe vector(0.2857, 0.4285, -0.8571)
    }
}