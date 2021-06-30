package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.math.equalsDelta
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.transformations.rotationY
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.transformations.viewTransform
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

internal class CameraTest {

    private val magic2 = sqrt(2.0) / 2.0

    @Test
    fun `Scenario Constructing a camera`() {
        val hSize = 160
        val vSize = 120
        val fieldOfView = PI / 2
        val c = Camera(hSize, vSize, fieldOfView)
        c.hSize shouldBe 160
        c.vSize shouldBe 120
        c.fieldOfView shouldBe PI / 2
        c.transform shouldBe identityMatrix
    }

    @Test
    fun `Scenario The pixel size for a horizontal canvas`() {
        val c = Camera(200, 125, PI / 2)
        c.pixelSize.equalsDelta(0.01) shouldBe true
    }

    @Test
    fun `Scenario The pixel size for a vertical canvas`() {
        val c = Camera(125, 200, PI / 2)
        c.pixelSize.equalsDelta(0.01) shouldBe true
    }

    @Test
    fun `Scenario Constructing a ray through the center of the canvas`() {
        val c = Camera(201, 101, PI / 2)
        val r = c.rayForPixel(100, 50)
        r.origin shouldBe point(0, 0, 0)
        r.direction shouldBe vector(0, 0, -1)
    }

    @Test
    fun `Scenario Constructing a ray through a corner of the canvas`() {
        val c = Camera(201, 101, PI / 2)
        val r = c.rayForPixel(0, 0)
        r.origin shouldBe point(0, 0, 0)
        r.direction shouldBe vector(0.66519, 0.33259, -0.66851)
    }

    @Test
    fun `Scenario Constructing a ray when the camera is transformed`() {
        val c = Camera(201, 101, PI / 2, rotationY(PI / 4) * translation(0, -2, 5))
        val r = c.rayForPixel(100, 50)
        r.origin shouldBe point(0, 2, -5)
        r.direction shouldBe vector(magic2, 0.0, -magic2)
    }

    @Test
    fun `Scenario Rendering a world with a camera`() {
        val w = defaultWorld()
        val from = point(0, 0, -5)
        val to = point(0, 0, 0)
        val up = vector(0, 1, 0)
        val transform = viewTransform(from, to, up)
        val c = Camera(11, 11, PI / 2, transform)
        val image = c.render(w)
        image.pixelAt(5, 5) shouldBe color(0.38066, 0.47583, 0.2855)
    }
}