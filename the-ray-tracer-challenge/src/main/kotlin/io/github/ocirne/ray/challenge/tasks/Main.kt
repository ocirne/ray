package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Canvas
import java.io.File
import kotlin.system.measureTimeMillis

fun renderScene(sceneNo: Int): Canvas {
    return when (sceneNo) {
        (1) -> box()
        (2) -> cover()
        (3) -> funWithPatterns1()
        (4) -> funWithPatterns2()
        (5) -> funWithReflection1()
        (6) -> pond()
        (7) -> scene()
        (8) -> silhouette()
        (9) -> silhouette3d()
        (10) -> spheresOnAPlane()
        (11) -> stripedSpheres()
        (12) -> funWithShapes1()
        else -> throw IllegalArgumentException("Unknown sceneNo $sceneNo")
    }
}

fun main() {
    val sceneNo = 12
    val timeInMillisRendering = measureTimeMillis {
        val canvas = renderScene(sceneNo)
        val timestamp = System.currentTimeMillis().toString()
        File("output/image${timestamp}.ppm").printWriter().use(canvas::toPPM)
    }
    println("rendering total $timeInMillisRendering ms")
}
