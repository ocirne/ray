package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

internal class TupleTest {

    @Test
    fun `Scenario A tuple with w eq 0 dot 1 is a point`() {
        val a = Tuple(4.3, -4.2, 3.1, 1.0)
        a.x shouldBe 4.3
        a.y shouldBe -4.2
        a.z shouldBe 3.1
        a.w shouldBe 1.0
        a.isPoint() shouldBe true
        a.isVector() shouldBe false
    }

    @Test
    fun `Scenario A tuple with w eq 0 is a vector`() {
        val a = Tuple(4.3, -4.2, 3.1, 0.0)
        a.x shouldBe 4.3
        a.y shouldBe -4.2
        a.z shouldBe 3.1
        a.w shouldBe 0.0
        a.isPoint() shouldBe false
        a.isVector() shouldBe true
    }

    @Test
    fun `Scenario point() creates tuples with w eq 1`() {
        val p = point(4, -4, 3)
        p shouldBe Tuple(4, -4, 3, 1)
    }

    @Test
    fun `Scenario vector() creates tuples with w eq 0`() {
        val v = vector(4, -4, 3)
        v shouldBe Tuple(4, -4, 3, 0)
    }

    @Test
    fun `Scenario Adding two tuples`() {
        val a1 = Tuple(3, -2, 5, 1)
        val a2 = Tuple(-2, 3, 1, 0)
        a1 + a2 shouldBe Tuple(1, 1, 6, 1)
    }

    @Test
    fun `Scenario Subtracting two points`() {
        val p1 = point(3, 2, 1)
        val p2 = point(5, 6, 7)
        p1 - p2 shouldBe vector(-2, -4, -6)
    }

    @Test
    fun `Scenario Subtracting a Vector from a point`() {
        val p = point(3, 2, 1)
        val v = vector(5, 6, 7)
        p - v shouldBe point(-2, -4, -6)
    }

    @Test
    fun `Scenario Subtracting two vectors`() {
        val v1 = vector(3, 2, 1)
        val v2 = vector(5, 6, 7)
        v1 - v2 shouldBe vector(-2, -4, -6)
    }

    @Test
    fun `Scenario Subtracting a vector from the zero vector`() {
        val zero = vector(0, 0, 0)
        val v = vector(1, -2, 3)
        zero - v shouldBe vector(-1, 2, -3)
    }

    @Test
    fun `Scenario Negating a tuple`() {
        val a = Tuple(1, -2, 3, -4)
        -a shouldBe Tuple(-1, 2, -3, 4)
    }

    @Test
    fun `Scenario Multiplying a tuple by a scalar`() {
        val a = Tuple(1, -2, 3, -4)
        a * 3.5 shouldBe Tuple(3.5, -7.0, 10.5, -14.0)
    }

    @Test
    fun `Scenario Multiplying a tuple by a fraction`() {
        val a = Tuple(1, -2, 3, -4)
        a * 0.5 shouldBe Tuple(0.5, -1.0, 1.5, -2.0)
    }

    @Test
    fun `Scenario Dividing a tuple by a scalar`() {
        val a = Tuple(1, -2, 3, -4)
        a / 2 shouldBe Tuple(0.5, -1.0, 1.5, -2.0)
    }

    @Test
    fun `Scenario Computing the magnitude of vector(1, 0, 0)`() {
        val v = vector(1, 0, 0)
        v.magnitude() shouldBe 1
    }

    @Test
    fun `Scenario Computing the magnitude of vector(0, 1, 0)`() {
        val v = vector(0, 1, 0)
        v.magnitude() shouldBe 1
    }

    @Test
    fun `Scenario Computing the magnitude of vector(0, 0, 1)`() {
        val v = vector(0, 0, 1)
        v.magnitude() shouldBe 1
    }

    @Test
    fun `Scenario Computing the magnitude of vector(1, 2, 3)`() {
        val v = vector(1, 2, 3)
        v.magnitude() shouldBe sqrt(14.0)
    }

    @Test
    fun `Scenario Computing the magnitude of vector(-1, -2, -3)`() {
        val v = vector(-1, -2, -3)
        v.magnitude() shouldBe sqrt(14.0)
    }

    @Test
    fun `Scenario Normalizing vector(4, 0, 0) gives (1, 0, 0)`() {
        val v = vector(4, 0, 0)
        v.normalize() shouldBe vector(1, 0, 0)
    }

    @Test
    fun `Scenario Normalizing vector(1, 2, 3)`() {
        val v = vector(1, 2, 3)
        // Vector(1 / √14, 2 / √14, 3 / √14)
        v.normalize() shouldBe vector(0.26726, 0.53452, 0.80178)
    }

    @Test
    fun `Scenario The magnitude of a normalized vector`() {
        val v = vector(1, 2, 3)
        val norm = v.normalize()
        norm.magnitude() shouldBe 1
    }

    @Test
    fun `Scenario The dot product of two tuples`() {
        val a = vector(1, 2, 3)
        val b = vector(2, 3, 4)
        a.dot(b) shouldBe 20
    }

    @Test
    fun `Scenario The cross product of two vectors`() {
        val a = vector(1, 2, 3)
        val b = vector(2, 3, 4)
        a.cross(b) shouldBe vector(-1, 2, -1)
        b.cross(a) shouldBe vector(1, -2, 1)
    }

    @Test
    fun `Scenario Colors are (red, green, blue) tuples`() {
        val c = color(-0.5, 0.4, 1.7)
        c.red shouldBe -0.5
        c.green shouldBe 0.4
        c.blue shouldBe 1.7
    }

    @Test
    fun `Scenario Adding colors`() {
        val c1 = color(0.9, 0.6, 0.75)
        val c2 = color(0.7, 0.1, 0.25)
        c1 + c2 shouldBe color(1.6, 0.7, 1.0)
    }

    @Test
    fun `Scenario Subtracting colors`() {
        val c1 = color(0.9, 0.6, 0.75)
        val c2 = color(0.7, 0.1, 0.25)
        c1 - c2 shouldBe color(0.2, 0.5, 0.5)
    }

    @Test
    fun `Scenario Multiplying a color by a scalar`() {
        val c = color(0.2, 0.3, 0.4)
        c * 2 shouldBe color(0.4, 0.6, 0.8)
    }

    @Test
    fun `Scenario Multiplying colors`() {
        val c1 = color(1.0, 0.2, 0.4)
        val c2 = color(0.9, 1.0, 0.1)
        c1 * c2 shouldBe color(0.9, 0.2, 0.04)
    }
}

/*
Scenario Reflecting a Vector approaching at 45°
  val v = Vector(1, -1, 0)
    And n = Vector(0, 1, 0)
  When r = reflect(v, n)
  Then r = Vector(1, 1, 0)

Scenario Reflecting a Vector off a slanted surface
  val v = Vector(0, -1, 0)
    And n = Vector(√2/2, √2/2, 0)
  When r = reflect(v, n)
  Then r = Vector(1, 0, 0)
*/