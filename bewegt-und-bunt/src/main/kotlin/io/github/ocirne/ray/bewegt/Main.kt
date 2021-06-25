package io.github.ocirne.ray.bewegt

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

    if (srec.is_specular) {
        return srec.attenuation *
                rayColor(srec.specular_ray!!, scene, depth - 1)
    }
    val light_ptr = hittable_pdf(scene.lights(), rec.p)
    val p = mixture_pdf(light_ptr, srec.pdf_ptr!!)

    val scattered = Ray(rec.p, p.generate(), r.time)
    val pdf_val = p.value(scattered.direction)

    return emitted + srec.attenuation *
            rec.mat.scattering_pdf(r, rec, scattered) *
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

fun renderScene(scene: Scene) {
    // Camera
    val camera = Camera(scene)

    val frame = RgbDataFrame(scene.imageWidth, scene.imageHeight)

    val timeInMillisRendering = measureTimeMillis {
        for (s in scene.samplesPerPixel - 1 downTo 0) {
            frame.incSamples()
            println("$s ")
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
    }
    println()
    println("rendering $timeInMillisRendering ms")
    val timeInMillisWriteToFile = measureTimeMillis {
        val timestamp = System.currentTimeMillis().toString()
        val filename = "output/imagetimestamp_scene_${scene}_${scene.samplesPerPixel}_samples"
        val ppm = PPM("$filename.ppm")
        ppm.writeToFile(frame)

        val gif = GIF("$filename.gif", scene.imageWidth, scene.imageHeight)
        gif.addDataFrame(frame)
        gif.writeToFile()
    }
    println("write to files $timeInMillisWriteToFile ms")
}

fun main() {
    val scene = AnimatableCornellBox(15.0)
    renderScene(scene)
}