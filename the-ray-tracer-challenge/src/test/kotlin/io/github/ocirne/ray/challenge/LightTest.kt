package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class LightsTest {

    @Test
    fun `Scenario A point light has a position and intensity`() {
        val intensity = color(1, 1, 1)
        val position = point(0, 0, 0)
        val light = PointLight(position, intensity)
        light.position shouldBe position
        light.intensity shouldBe intensity
    }
}