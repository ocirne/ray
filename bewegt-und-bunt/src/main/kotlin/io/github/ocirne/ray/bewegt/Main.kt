package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.canvas.*
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
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
            rayColor(scattered, background, world, lights, depth-1) / pdf_val
}

fun init_scene(sceneNo: Int) {
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

    world = scene.world()
    aperture = scene.aperture
    aspect_ratio = scene.aspect_ratio
    background = scene.background
    image_width = scene.image_width
    lookat = scene.lookAt
    lookfrom = scene.lookFrom
    samples_per_pixel = scene.samples_per_pixel
    vfov = scene.vfov
}

// Image
var aspect_ratio = 16.0 / 9.0
var image_width = 600
var samples_per_pixel = 100
const val max_depth = 50

// World
var world: hittable_list? = null
var lookfrom: Point3? = null
var lookat: Point3? = null
var vup = Vector3(0, 1, 0)
var dist_to_focus = 10.0
var vfov = 40.0
var aperture = 0.0
var background = BLACK
var time0 = 0.0
var time1 = 1.0

// Render
val scene = 9

fun main() {
    init_scene(scene)

    val lights = hittable_list.builder()
        .add(xz_rect(213, 343, 227, 332, 554, material()))
        .add(sphere(Point3(190, 90, 190), 90, material()))
        .build()

    // Camera
    val image_height = (image_width / aspect_ratio).toInt()
    val cam = camera(lookfrom!!, lookat!!, vup, vfov, aspect_ratio, aperture, dist_to_focus, time0, time1)

    val frame = RgbDataFrame(image_width, image_height)

    val timeInMillisRendering = measureTimeMillis {
        for (s in samples_per_pixel-1 downTo 0) {
            frame.incSamples()
            println("$s ")
            for (y in 0 until image_height) {
                for (x in 0 until image_width) {
                    val u = (x + Random.nextDouble()) / (image_width - 1)
                    val v = (y + Random.nextDouble()) / (image_height - 1)
                    val r = cam.get_ray(u, v)
                    val pixelColor = rayColor(r, background, world!!, lights, max_depth)
                    frame.plus(x, y, pixelColor)
                }
            }
        }
    }
    println()
    println("rendering $timeInMillisRendering ms")
    val timeInMillisWriteToFile = measureTimeMillis {
        val timestamp = System.currentTimeMillis().toString()
        val filename = "output/image${timestamp}_scene_${scene}_${samples_per_pixel}_samples"
        val ppm = PPM("$filename.ppm")
        ppm.writeToFile(frame)

        val gif = GIF("$filename.gif", image_width, image_height)
        gif.addDataFrame(frame)
        gif.writeToFile()
    }
    println("write to files $timeInMillisWriteToFile ms")
}
