package io.github.ocirne.ray.bewegt.canvas

import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.ImageOptions
import java.io.FileOutputStream

class GIF(filename: String, private val width: Int, private val height: Int) {

    private val outputStream = FileOutputStream(filename)
    private val options = ImageOptions()
    private val gifEncoder = GifEncoder(outputStream, width, height, 0)

    fun addDataFrame(rgbDataFrame: RgbDataFrame) {
        require(rgbDataFrame.width == width)
        require(rgbDataFrame.height == height)
        val gifDataFrame = Array(rgbDataFrame.height) { IntArray(rgbDataFrame.width) { 0 } }
        for (y in 0 until rgbDataFrame.height) {
            for (x in 0 until rgbDataFrame.width) {
                gifDataFrame[y][x] = rgbDataFrame.atAsInt(x, rgbDataFrame.height - 1 - y)
            }
        }
        gifEncoder.addImage(gifDataFrame, options)
    }

    fun writeToFile() {
        gifEncoder.finishEncoding()
        outputStream.close()
    }
}
