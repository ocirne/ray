package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.camera.Camera
import io.github.ocirne.ray.bewegt.canvas.*
import io.github.ocirne.ray.bewegt.hittable.Hittable
import io.github.ocirne.ray.bewegt.math.HittablePDF
import io.github.ocirne.ray.bewegt.math.MixturePDF
import io.github.ocirne.ray.bewegt.math.Ray
import io.github.ocirne.ray.bewegt.math.infinity
import io.github.ocirne.ray.bewegt.scene.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun rayColor(r: Ray, background: RGBColor, world: Hittable, lights: Hittable, depth: Int): RGBColor {
    if (depth <= 0) {
        return NO_COLOR
    }
    // If the ray hits nothing, return the background Color.
    val hitRecord = world.hit(r, 0.001, infinity) ?: return background

    val emitted = hitRecord.material.emitted(r, hitRecord, hitRecord.u, hitRecord.v, hitRecord.p)
    val scatterRecord = hitRecord.material.scatter(r, hitRecord) ?: return emitted

    scatterRecord.specularRay?.let {
        return scatterRecord.attenuation * rayColor(scatterRecord.specularRay, background, world, lights, depth - 1)
    }
    val light = HittablePDF(lights, hitRecord.p)
    val p = MixturePDF(light, scatterRecord.pdf!!)

    val scattered = Ray(hitRecord.p, p.generate(), r.time)
    val pdfValue = p.value(scattered.direction)

    return emitted + scatterRecord.attenuation *
            hitRecord.material.scatteringPdf(r, hitRecord, scattered) *
            rayColor(scattered, background, world, lights, depth - 1) / pdfValue
}

fun initScene(sceneNo: Int): Scene {
    return when (sceneNo) {
        (1) -> RandomSceneWeekend()
        (2) -> TwoSpheres()
        (3) -> TwoPerlinSpheres()
        (4) -> Earth()
        (5) -> SimpleLight()
        (6) -> CornellBox()
        (7) -> CornellBoxSmoke()
        (8) -> FinalSceneNextWeek()
        (9) -> CornellBoxBook3()
        (10) -> AnimatableCornellBox(0.0)
        (11) -> SolarEclipse()
        else -> throw UnsupportedOperationException()
    }
}

fun renderFrame(scene: Scene): RGBDataFrame {
    // Camera
    val camera = Camera(scene)
    val frame = RGBDataFrame(scene.imageWidth, scene.imageHeight)

    val world = scene.buildWorld()
    val lights = scene.buildLights()

    for (s in scene.samplesPerPixel - 1 downTo 0) {
        frame.incSamples()
        if (s % 10 == 0) {
            print("$s ")
        }
        for (y in 0 until scene.imageHeight) {
            for (x in 0 until scene.imageWidth) {
                val u = (x + Random.nextDouble()) / (scene.imageWidth - 1)
                val v = (y + Random.nextDouble()) / (scene.imageHeight - 1)
                val r = camera.getRay(u, v)
                val pixelColor = rayColor(r, scene.background, world, lights, scene.maxDepth)
                frame.plus(x, y, pixelColor)
            }
        }
    }
    println()
    return frame
}

fun renderAnimatedCornellBox() {
    val timeInMillisRendering = measureTimeMillis {
        val nullScene = AnimatableCornellBox(0.0)
        val filename = "output/image${nullScene.javaClass.simpleName}_${nullScene.samplesPerPixel}_spf"

        val gif = GIF("$filename.gif", nullScene.imageWidth, nullScene.imageHeight)
        for (i in 1..30) {
            print("Frame $i: ")
            val scene = AnimatableCornellBox(15.0 + 3*i)
            val frame = renderFrame(scene)
            gif.addDataFrame(frame)
        }
        gif.writeToFile()
    }
    println("rendering $timeInMillisRendering ms")
}

fun renderAnimatedEarth() {
    val timeInMillisRendering = measureTimeMillis {
        val nullScene = Earth(0.0)
        val filename = "output/image${nullScene.javaClass.simpleName}_${nullScene.samplesPerPixel}_spf"

        val gif = GIF("$filename.gif", nullScene.imageWidth, nullScene.imageHeight)
        for (i in 1..30) {
            print("Frame $i: ")
            val scene = Earth(15.0 + 3*i)
            val frame = renderFrame(scene)
            gif.addDataFrame(frame)
        }
        gif.writeToFile()
    }
    println("rendering $timeInMillisRendering ms")
}

fun renderScene(sceneNo: Int) {
    val timeInMillisRendering = measureTimeMillis {
        val nullScene = initScene(sceneNo)
        val filename = "output/image${nullScene.javaClass.simpleName}_${nullScene.samplesPerPixel}_spf"

        val gif = GIF("$filename.gif", nullScene.imageWidth, nullScene.imageHeight)
        val scene = initScene(sceneNo)
        val frame = renderFrame(scene)
        gif.addDataFrame(frame)
        gif.writeToFile()
    }
    println("rendering $timeInMillisRendering ms")
}

fun main() {
    renderAnimatedCornellBox()
//    for (i in 1..10) {
//        renderScene(i)
//    }
//    renderScene(11)
//    renderAnimatedEarth()
}
