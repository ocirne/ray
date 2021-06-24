package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.math.Color
import io.github.ocirne.ray.bewegt.math.Vector3
import javax.imageio.ImageIO

class image_texture(filename: String) :  texture {

    val file_stream = image_texture::class.java.classLoader.getResourceAsStream(filename)
    val data = if (file_stream != null) ImageIO.read(file_stream) else null
    val width = data?.width ?: 0
    val height = data?.height ?: 0

    override fun value(u: Double, v: Double, p: Vector3): Color {
        // If we have no texture data, then return solid cyan as a debugging aid.
        if (data == null)
            return Color(0, 1, 1)

        // Clamp input texture coordinates to [0,1] x [1,0]
        val u_clamped = u.clamp(0.0, 1.0)
        val v_clamped = 1.0 - v.clamp(0.0, 1.0)  // Flip V to image coordinates

        var i = (u_clamped * width).toInt()
        var j = (v_clamped * height).toInt()

        // Clamp integer mapping, since actual coordinates should be less than 1.0
        if (i >= width) i = width - 1
        if (j >= height) j = height - 1

        val color_scale = 1.0 / 255.0
        val pixel: Int = data.getRGB(i, j)
        return Color(color_scale * pixel.r(), color_scale * pixel.g(), color_scale * pixel.b())
    }
}
