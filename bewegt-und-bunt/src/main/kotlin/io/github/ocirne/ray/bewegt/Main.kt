package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.camera.Camera
import io.github.ocirne.ray.bewegt.canvas.*
import io.github.ocirne.ray.bewegt.math.Ray
import io.github.ocirne.ray.bewegt.scene.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis


fun rayColor(r: Ray, scene: Scene, depth: Int=scene.maxDepth): RgbColor {
    if (depth <= 0) {
        return NO_COLOR
    }
    // If the ray hits nothing, return the background Color.
    val rec = scene.world().hit(r, 0.001, infinity) ?: return scene.background

    val emitted = rec.mat.emitted(r, rec, rec.u, rec.v, rec.p)
    val srec = rec.mat.scatter(r, rec) ?: return emitted

    srec.specularRay?.let {
        return srec.attenuation * rayColor(srec.specularRay, scene, depth - 1)
    }
    val light_ptr = hittable_pdf(scene.lights(), rec.p)
    val p = mixture_pdf(light_ptr, srec.pdf!!)

    val scattered = Ray(rec.p, p.generate(), r.time)
    val pdf_val = p.value(scattered.direction)

    return emitted + srec.attenuation *
            rec.mat.scatteringPdf(r, rec, scattered) *
            rayColor(scattered, scene, depth - 1) / pdf_val
}

fun init_scene(sceneNo: Int): Scene {
    val scene: Scene = when (sceneNo) {
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
        else -> throw UnsupportedOperationException()
    }
    return scene
}

fun renderFrame(scene: Scene): RgbDataFrame {
    // Camera
    val camera = Camera(scene)
    val frame = RgbDataFrame(scene.imageWidth, scene.imageHeight)

    for (s in scene.samplesPerPixel - 1 downTo 0) {
        frame.incSamples()
        print("$s ")
        for (y in 0 until scene.imageHeight) {
            for (x in 0 until scene.imageWidth) {
                val u = (x + Random.nextDouble()) / (scene.imageWidth - 1)
                val v = (y + Random.nextDouble()) / (scene.imageHeight - 1)
                val r = camera.getRay(u, v)
                val pixelColor = rayColor(r, scene, scene.maxDepth)
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

fun renderScene(sceneNo: Int) {
    val timeInMillisRendering = measureTimeMillis {
        val nullScene = init_scene(sceneNo)
        val filename = "output/image${nullScene.javaClass.simpleName}_${nullScene.samplesPerPixel}_spf"

        val gif = GIF("$filename.gif", nullScene.imageWidth, nullScene.imageHeight)
        val scene = init_scene(sceneNo)
        val frame = renderFrame(scene)
        gif.addDataFrame(frame)
        gif.writeToFile()
    }
    println("rendering $timeInMillisRendering ms")
}

fun main() {
    // renderAnimatedCornellBox()
    renderScene(3)
}
