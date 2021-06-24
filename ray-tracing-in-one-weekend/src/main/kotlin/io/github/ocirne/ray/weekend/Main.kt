package io.github.ocirne.ray.weekend

import java.io.File
import java.io.PrintWriter
import kotlin.random.Random
import kotlin.system.measureTimeMillis


fun ray_color(r: ray, background: color, world: hittable, lights: hittable, depth: Int): color {
    if (depth <= 0) {
        return color(0, 0, 0)
    }
    // If the ray hits nothing, return the background color.
    val rec = world.hit(r, 0.001, infinity) ?: return background

    val emitted = rec.mat.emitted(r, rec, rec.u, rec.v, rec.p)
    val srec = rec.mat.scatter(r, rec) ?: return emitted

    if (srec.is_specular) {
        return srec.attenuation *
                ray_color(srec.specular_ray!!, background, world, lights, depth - 1)
    }

    val light_ptr = hittable_pdf(lights, rec.p)
    val p = mixture_pdf(light_ptr, srec.pdf_ptr!!)

    val scattered = ray(rec.p, p.generate(), r.time())
    val pdf_val = p.value(scattered.direction())

    return emitted + srec.attenuation *
            rec.mat.scattering_pdf(r, rec, scattered) *
            ray_color(scattered, background, world, lights, depth-1) / pdf_val
}

fun random_scene(): hittable_list {
    val builder = hittable_list.builder()

    val checker = checker_texture(color(0.2, 0.3, 0.1), color(0.9, 0.9, 0.9))
    builder.add(sphere(point3(0,-1000,0), 1000, lambertian(checker)))

    for (a in -11..10) {
        for (b in -11..10) {
            val choose_mat = Random.nextDouble()
            val center = point3(a + 0.9* Random.nextDouble(), 0.2, b + 0.9*Random.nextDouble())

            if ((center - point3(4.0, 0.2, 0.0)).length() > 0.9) {
                if (choose_mat < 0.8) {
                    // diffuse
                    val albedo = color.random() * color.random()
                    val sphere_material = lambertian(albedo)
                    val center2 = center + vec3(0.0, Random.nextDouble(0.0, 0.5), 0.0)
                    builder.add(moving_sphere(center, center2, 0.0, 1.0, 0.2, sphere_material))
                } else if (choose_mat < 0.95) {
                    // metal
                    val albedo = color.random(0.5, 1.0)
                    val fuzz = Random.nextDouble(0.5)
                    val sphere_material = metal(albedo, fuzz)
                    builder.add(sphere(center, 0.2, sphere_material))
                } else {
                    // glass
                    val sphere_material = dielectric(1.5)
                    builder.add(sphere(center, 0.2, sphere_material))
                }
            }
        }
    }

    val material1 = dielectric(1.5)
    builder.add(sphere(point3(0, 1, 0), 1, material1))

    val material2 = lambertian(color(0.4, 0.2, 0.1))
    builder.add(sphere(point3(-4, 1, 0), 1, material2))

    val material3 = metal(color(0.7, 0.6, 0.5), 0)
    builder.add(sphere(point3(4, 1, 0), 1, material3))

    return builder.build()
}

fun two_spheres(): hittable_list {
    val objects = hittable_list.builder()

    val checker = checker_texture(color(0.2, 0.3, 0.1), color(0.9, 0.9, 0.9))

    objects.add(sphere(point3(0,-10, 0), 10, lambertian(checker)))
    objects.add(sphere(point3(0, 10, 0), 10, lambertian(checker)))

    return objects.build()
}

fun two_perlin_spheres(): hittable_list {
    val objects = hittable_list.builder()

    val pertext = noise_texture(4.0)
    objects.add(sphere(point3(0,-1000,0), 1000, lambertian(pertext)))
    objects.add(sphere(point3(0, 2, 0), 2, lambertian(pertext)))

    return objects.build()
}

fun earth(): hittable_list {
    val objects = hittable_list.builder()

    val earth_texture = image_texture("earthmap.jpg")
    val earth_surface = lambertian(earth_texture)
    val globe = sphere(point3(0,0,0), 2, earth_surface)
    objects.add(globe)

    return objects.build()
}

fun simple_light(): hittable_list {
    val objects = hittable_list.builder()

    val pertext = noise_texture(4.0)
    objects.add(sphere(point3(0,-1000,0), 1000, lambertian(pertext)))
    objects.add(sphere(point3(0,2,0), 2, lambertian(pertext)))

    val difflight = diffuse_light(color(4,4,4))
    objects.add(xy_rect(3, 5, 1, 3, -2, difflight))

    return objects.build()
}

fun cornell_box(): hittable_list {
    val objects = hittable_list.builder()

    val red   = lambertian(color(.65, .05, .05))
    val white = lambertian(color(.73, .73, .73))
    val green = lambertian(color(.12, .45, .15))
    val light = diffuse_light(color(15, 15, 15))

    objects.add(yz_rect(0, 555, 0, 555, 555, green))
    objects.add(yz_rect(0, 555, 0, 555, 0, red))
    objects.add(xz_rect(213, 343, 227, 332, 554, light))
    objects.add(xz_rect(0, 555, 0, 555, 0, white))
    objects.add(xz_rect(0, 555, 0, 555, 555, white))
    objects.add(xy_rect(0, 555, 0, 555, 555, white))

    var box1: hittable = box(point3(0, 0, 0), point3(165, 330, 165), white)
    box1 = rotate_y(box1, 15.0)
    box1 = translate(box1, vec3(265,0,295))
    objects.add(box1)

    var box2: hittable = box(point3(0,0,0), point3(165,165,165), white)
    box2 = rotate_y(box2, -18.0)
    box2 = translate(box2, vec3(130,0,65))
    objects.add(box2)

    return objects.build()
}

fun cornell_smoke(): hittable_list {
    val objects = hittable_list.builder()

    val red   = lambertian(color(.65, .05, .05))
    val white = lambertian(color(.73, .73, .73))
    val green = lambertian(color(.12, .45, .15))
    val light = diffuse_light(color(7, 7, 7))

    objects.add(yz_rect(0, 555, 0, 555, 555, green))
    objects.add(yz_rect(0, 555, 0, 555, 0, red))
    objects.add(xz_rect(113, 443, 127, 432, 554, light))
    objects.add(xz_rect(0, 555, 0, 555, 555, white))
    objects.add(xz_rect(0, 555, 0, 555, 0, white))
    objects.add(xy_rect(0, 555, 0, 555, 555, white))

    var box1: hittable = box(point3(0,0,0), point3(165,330,165), white)
    box1 = rotate_y(box1, 15.0)
    box1 = translate(box1, vec3(265,0,295))

    var box2: hittable = box(point3(0,0,0), point3(165,165,165), white)
    box2 = rotate_y(box2, -18.0)
    box2 = translate(box2, vec3(130,0,65))

    objects.add(constant_medium(box1, 0.01, color(0,0,0)))
    objects.add(constant_medium(box2, 0.01, color(1,1,1)))

    return objects.build()
}

fun final_scene(): hittable_list {
    val boxes1 = hittable_list.builder()
    val ground = lambertian(color(0.48, 0.83, 0.53))

    val boxes_per_side = 20
    for (i in 0 until boxes_per_side) {
        for (j in 0 until boxes_per_side) {
            val w = 100.0
            val x0 = -1000.0 + i*w
            val z0 = -1000.0 + j*w
            val y0 = 0.0
            val x1 = x0 + w
            val y1 = Random.nextDouble(1.0, 101.0)
            val z1 = z0 + w

            boxes1.add(box(point3(x0, y0, z0), point3(x1, y1, z1), ground))
        }
    }
    val objects = hittable_list.builder()

    objects.add(bvh_node(boxes1.build(), time0 = 0.0, time1 = 1.0))

    val light = diffuse_light(color(7, 7, 7))
    objects.add(xz_rect(123, 423, 147, 412, 554, light))

    val center1 = point3(400, 400, 200)
    val center2 = center1 + vec3(30,0,0)
    val moving_sphere_material = lambertian(color(0.7, 0.3, 0.1))
    objects.add(moving_sphere(center1, center2, 0.0, 1.0, 50.0, moving_sphere_material))

    objects.add(sphere(point3(260, 150, 45), 50, dielectric(1.5)))
    objects.add(sphere(
        point3(0, 150, 145), 50, metal(color(0.8, 0.8, 0.9), 1.0)
    ))

    val boundary1 = sphere(point3(360,150,145), 70, dielectric(1.5))
    objects.add(boundary1)
    objects.add(constant_medium(boundary1, 0.2, color(0.2, 0.4, 0.9)))
    val boundary2 = sphere(point3(0, 0, 0), 5000, dielectric(1.5))
    objects.add(constant_medium(boundary2, .0001, color(1,1,1)))

    val emat = lambertian(image_texture("earthmap.jpg"))
    objects.add(sphere(point3(400,200,400), 100, emat))
    val pertext = noise_texture(0.1)
    objects.add(sphere(point3(220,280,300), 80, lambertian(pertext)))

    val boxes2 = hittable_list.builder()
    val white = lambertian(color(.73, .73, .73))
    val ns = 1000
    for (j in 0 until ns) {
        boxes2.add(sphere(point3.random(0.0, 165.0), 10, white))
    }

    objects.add(translate(rotate_y(bvh_node(boxes2.build(), time0 = 0.0, time1 =1.0), 15.0), vec3(-100,270,395)))

    return objects.build()
}

fun cornell_box_book3(): hittable_list {
    val objects = hittable_list.builder()

    val red      = lambertian(color(.65, .05, .05))
    val white    = lambertian(color(.73, .73, .73))
    val green    = lambertian(color(.12, .45, .15))
    val light    = diffuse_light(color(15, 15, 15))
    val aluminum = metal(color(0.8, 0.85, 0.88), 0.0)
    val glass    = dielectric(1.5)

    objects.add(yz_rect(0, 555, 0, 555, 555, green))
    objects.add(yz_rect(0, 555, 0, 555, 0, red))
    objects.add(flip_face(xz_rect(213, 343, 227, 332, 554, light)))
    objects.add(xz_rect(0, 555, 0, 555, 555, white))
    objects.add(xz_rect(0, 555, 0, 555, 0, white))
    objects.add(xy_rect(0, 555, 0, 555, 555, white))

    var box1: hittable = box(point3(0,0,0), point3(165,330,165), aluminum)
    box1 = rotate_y(box1, 15.0)
    box1 = translate(box1, vec3(265,0,295))
    objects.add(box1)

    objects.add(sphere(point3(190,90,190), 90 , glass))

    return objects.build()
}

fun init_scene(scene: Int) {
    when (scene) {
        (1) -> {
            world = random_scene()
            background = color(0.7, 0.8, 1.0)
            lookfrom = point3(13, 2, 3)
            lookat = point3(0, 0, 0)
            vfov = 20.0
            aperture = 0.1
        }
        (2) -> {
            world = two_spheres()
            background = color(0.7, 0.8, 1.0)
            lookfrom = point3(13,2,3)
            lookat = point3(0,0,0)
            vfov = 20.0
        }
        (3) -> {
            world = two_perlin_spheres()
            background = color(0.7, 0.8, 1.0)
            lookfrom = point3(13,2,3)
            lookat = point3(0,0,0)
            vfov = 20.0
        }
        (4) -> {
            world = earth()
            background = color(0.7, 0.8, 1.0)
            lookfrom = point3(13, 2, 3)
            lookat = point3(0, 0, 0)
            vfov = 20.0
        }
        (5) -> {
            world = simple_light()
            samples_per_pixel = 100
            background = color(0, 0, 0)
            lookfrom = point3(26,3,6)
            lookat = point3(0,2,0)
            vfov = 20.0
        }
        (6) -> {
            world = cornell_box()
            aspect_ratio = 1.0
            image_width = 600
            samples_per_pixel = 200
            background = color(0, 0, 0)
            lookfrom = point3(278, 278, -800)
            lookat = point3(278, 278, 0)
            vfov = 40.0
        }
        (7) -> {
            world = cornell_smoke()
            aspect_ratio = 1.0
            image_width = 200 // 600
            samples_per_pixel = 200
            lookfrom = point3(278, 278, -800)
            lookat = point3(278, 278, 0)
            vfov = 40.0
        }
        (8) -> {
            world = final_scene()
            aspect_ratio = 1.0
            image_width = 200 // 800
            samples_per_pixel = 20  // 10000
            background = color(0, 0, 0)
            lookfrom = point3(478, 278, -600)
            lookat = point3(278, 278, 0)
            vfov = 40.0
        }
        (9) -> {
            world = cornell_box_book3()
            aspect_ratio = 1.0 / 1.0
            image_width = 600
            samples_per_pixel = 1000
            background = color(0, 0, 0)
            lookfrom = point3(278, 278, -800)
            lookat = point3(278, 278, 0)
            vfov = 40.0
        }
        else -> throw UnsupportedOperationException()
    }
}

// Image
var aspect_ratio = 16.0 / 9.0
var image_width = 600
var samples_per_pixel = 100
const val max_depth = 50

// World
var world: hittable_list? = null
var lookfrom: point3? = null
var lookat: point3? = null
var vup = vec3(0, 1, 0)
var dist_to_focus = 10.0
var vfov = 40.0
var aperture = 0.0
var background = color(0, 0, 0)
var time0 = 0.0
var time1 = 1.0

// Render
val scene = 9

fun run(out: PrintWriter) {
    init_scene(scene)

    val lights = hittable_list.builder()
        .add(xz_rect(213, 343, 227, 332, 554, material()))
        .add(sphere(point3(190, 90, 190), 90, material()))
        .build()

    // Camera
    val image_height = (image_width / aspect_ratio).toInt()
    val cam = camera(lookfrom!!, lookat!!, vup, vfov, aspect_ratio, aperture, dist_to_focus, time0, time1)

    out.println("P3")
    out.println("$image_width $image_height")
    out.println("255")

    for (j in image_height-1 downTo 0) {
        println("$j ")
        for (i in 0 until image_width) {
            var pixel_color = color(0, 0, 0)
            for (s in 0..samples_per_pixel) {
                val u = (i + Random.nextDouble()) / (image_width - 1)
                val v = (j + Random.nextDouble()) / (image_height - 1)
                val r = cam.get_ray(u, v)
                pixel_color += ray_color(r, background, world!!, lights, max_depth)
            }
            write_color(out, pixel_color, samples_per_pixel)
        }
    }
    println()
}

fun main() {
    val timestamp = System.currentTimeMillis().toString()
    val timeInMillis = measureTimeMillis {
        File("output/image${timestamp}_scene_${scene}_${samples_per_pixel}_samples.ppm").printWriter().use(::run)
    }
    println("elapsed $timeInMillis ms")
}
