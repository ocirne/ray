package io.github.ocirne.ray.bewegt.texture

import io.github.ocirne.ray.bewegt.canvas.CYAN
import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.clamp
import javax.imageio.ImageIO
import kotlin.math.min

class ImageTexture(filename: String) : Texture {

    private val fileStream = ImageTexture::class.java.classLoader.getResourceAsStream(filename)
    private val data = if (fileStream != null) ImageIO.read(fileStream) else null
    private val width = data?.width ?: 0
    private val height = data?.height ?: 0

    override fun value(u: Double, v: Double, p: Vector3): RGBColor {
        // If we have no texture data, then return solid cyan as a debugging aid.
        if (data == null) {
            return CYAN
        }
        // Clamp input texture coordinates to [0,1] x [1,0]
        val uClamped = u.clamp(0.0, 1.0)
        val vClamped = 1.0 - v.clamp(0.0, 1.0)  // Flip V to image coordinates

        // Clamp integer mapping, since actual coordinates should be less than 1.0
        val i = min(width - 1, (uClamped * width).toInt())
        val j = min(height - 1, (vClamped * height).toInt())

        val colorScale = 1.0 / 255.0
        val pixel = data.getRGB(i, j)
        return RGBColor(colorScale * pixel.r(), colorScale * pixel.g(), colorScale * pixel.b())
    }

    private fun Int.r(): Int = (this shr 16) and 0xFF

    private fun Int.g(): Int = (this shr 8) and 0xFF

    private fun Int.b(): Int = this and 0xFF
}
