package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import java.io.StringWriter

internal class CanvasTest {

    @Test
    fun `Scenario Creating a canvas`() {
        val c = Canvas(10, 20)
        c.width shouldBe 10
        c.height shouldBe 20
        for (x in 0 until 10) {
            for (y in 0 until 20) {
                c.pixelAt(x, y) shouldBe BLACK
            }
        }
    }

    @Test
    fun `Scenario Writing pixels to a canvas`() {
        val c = Canvas(10, 20)
        val red = color(1, 0, 0)
        c.writePixel(2, 3, red)
        c.pixelAt(2, 3) shouldBe red
    }

    @Test
    fun `Scenario Constructing the PPM header`() {
        val c = Canvas(5, 3)
        val ppm = convertToPPM(c)
        ppm.lines().slice(0..2) shouldBe listOf("P3", "5 3", "255")
    }

    @Test
    fun `Scenario Constructing the PPM pixel data`() {
        val c = Canvas(5, 3)
        val c1 = color(1.5, 0.0, 0.0)
        val c2 = color(0.0, 0.5, 0.0)
        val c3 = color(-0.5, 0.0, 1.0)
        c.writePixel(0, 0, c1)
        c.writePixel(2, 1, c2)
        c.writePixel(4, 2, c3)
        val ppm = convertToPPM(c)
        println(ppm)
        ppm.lines().slice(3..17) shouldBe listOf(
            "255 0 0", "0 0 0", "0 0 0", "0 0 0", "0 0 0",
            "0 0 0", "0 0 0", "0 128 0", "0 0 0", "0 0 0",
            "0 0 0", "0 0 0", "0 0 0", "0 0 0", "0 0 255"
        )
    }

    @Test
    fun `Scenario Splitting long lines in PPM files`() {
        val c = Canvas(10, 2, color(1.0, 0.8, 0.6))
        val ppm = convertToPPM(c)
        ppm.lines().slice(3..22) shouldBe listOf(
            "255 204 153", "255 204 153", "255 204 153", "255 204 153",
            "255 204 153", "255 204 153", "255 204 153", "255 204 153",
            "255 204 153", "255 204 153", "255 204 153", "255 204 153",
            "255 204 153", "255 204 153", "255 204 153", "255 204 153",
            "255 204 153", "255 204 153", "255 204 153", "255 204 153"
        )
    }

    @Test
    fun `Scenario PPM files are terminated by a newline character`() {
        val c = Canvas(5, 3)
        val ppm = convertToPPM(c)
        ppm.shouldEndWith("\n")
    }

    private fun convertToPPM(c: Canvas): String {
        val writer = StringWriter()
        c.toPPM(PrintWriter(writer))
        return writer.toString()
    }
}
