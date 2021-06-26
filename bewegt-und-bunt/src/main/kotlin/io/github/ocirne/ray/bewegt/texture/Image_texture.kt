package io.github.ocirne.ray.bewegt.texture

import io.github.ocirne.ray.bewegt.*
import io.github.ocirne.ray.bewegt.canvas.CYAN
import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.math.Vector3
import javax.imageio.ImageIO

class image_texture(filename: String) : texture {

    val file_stream = image_texture::class.java.classLoader.getResourceAsStream(filename)
    val data = if (file_stream != null) ImageIO.read(file_stream) else null
    val width = data?.width ?: 0
    val height = data?.height ?: 0

    override fun value(u: Double, v: Double, p: Vector3): RgbColor {
        // If we have no texture data, then return solid cyan as a debugging aid.
        if (data == null)
            return CYAN

        // Clamp input texture coordinates to [0,1] x [1,0]
        val u_clamped = u.clamp(0.0, 1.0)
        val v_clamped = 1.0 - v.clamp(0.0, 1.0)  // Flip V to image coordinates

        var i = (u_clamped * width).toInt()
        var j = (v_clamped * height).toInt()

        // Clamp integer mapping, since actual coordinates should be less than 1.0
        if (i >= width) i = width - 1
        if (j >= height) j = height - 1

        val colorScale = 1.0 / 255.0
        val pixel: Int = data.getRGB(i, j)
        return RgbColor(colorScale * pixel.r(), colorScale * pixel.g(), colorScale * pixel.b())
    }
}
