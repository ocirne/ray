package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Canvas
import java.io.File
import kotlin.system.measureTimeMillis

fun renderScene(sceneNo: Int): Canvas {
    return when (sceneNo) {
        1 -> box()
        2 -> cover()
        3 -> funWithPatterns1()
        4 -> funWithPatterns2()
        5 -> funWithReflection1()
        6 -> pond()
        7 -> scene()
        8 -> silhouette()
        9 -> silhouette3d()
        10 -> spheresOnAPlane()
        11 -> stripedSpheres()
        12 -> funWithShapes1()
        13 -> funWithPatterns3()
        14 -> boundsVisualization()
        15 -> boundsVisualization2() // 188 seconds
        16 -> boundsVisualization2Opt()  // 64 seconds
        17 -> hexagonMain()
        18 -> funWithTriangles1()
        19 -> funWithObjFiles1()
        20 -> funWithSmoothing1()
        21 -> funWithCSG1()
        22 -> funWithCSG2()
        23 -> funWithLeoCAD1()
        24 -> funWithLeoCAD2()
        else -> throw IllegalArgumentException("Unknown sceneNo $sceneNo")
    }
}

fun main() {
    val sceneNo = 23
    val timeInMillisRendering = measureTimeMillis {
        val canvas = renderScene(sceneNo)
        val timestamp = System.currentTimeMillis().toString()
        File("output/image$timestamp.ppm").printWriter().use(canvas::toPPM)
    }
    println("rendering total $timeInMillisRendering ms")
}

// TODO
// - fractale (Lindenmeyer Trees)
// - Materialen vererben
//