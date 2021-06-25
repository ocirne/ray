package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.canvas.*
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Ray
import io.github.ocirne.ray.bewegt.scene.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis


fun rayColor(r: Ray, background: RgbColor, world: hittable, lights: hittable, depth: Int): RgbColor {
    if (depth <= 0) {
        return NO_COLOR
    }
    // If the ray hits nothing, return the background Color.
    val rec = world.hit(r, 0.001, infinity) ?: return background

    val emitted = rec.mat.emitted(r, rec, rec.u, rec.v, rec.p)
    val srec = rec.mat.scatter(r, rec) ?: return emitted

    if (srec.is_specular) {
        return srec.attenuation *
                rayColor(srec.specular_ray!!, background, world, lights, depth - 1)
    }

    val light_ptr = hittable_pdf(lights, rec.p)
    val p = mixture_pdf(light_ptr, srec.pdf_ptr!!)

    val scattered = Ray(rec.p, p.generate(), r.time)
    val pdf_val = p.value(scattered.direction)

    return emitted + srec.attenuation *
            rec.mat.scattering_pdf(r, rec, scattered) *
            rayColor(scattered, background, world, lights, depth - 1) / pdf_val
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
        else -> throw UnsupportedOperationException()
    }
    return scene
}

// Render
val sceneNo = 9

fun main() {
    val scene = init_scene(sceneNo)

    val lights = hittable_list.builder()
        .add(xz_rect(213, 343, 227, 332, 554, material()))
        .add(sphere(Point3(190, 90, 190), 90, material()))
        .build()

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
                    val pixelColor = rayColor(r, scene.background, scene.world(), lights, scene.maxDepth)
                    frame.plus(x, y, pixelColor)
                }
            }
        }
    }
    println()
    println("rendering $timeInMillisRendering ms")
    val timeInMillisWriteToFile = measureTimeMillis {
        val timestamp = System.currentTimeMillis().toString()
        val filename = "output/image${timestamp}_scene_${scene}_${scene.samplesPerPixel}_samples"
        val ppm = PPM("$filename.ppm")
        ppm.writeToFile(frame)

        val gif = GIF("$filename.gif", scene.imageWidth, scene.imageHeight)
        gif.addDataFrame(frame)
        gif.writeToFile()
    }
    println("write to files $timeInMillisWriteToFile ms")
}
