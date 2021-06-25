package io.github.ocirne.ray.bewegt.animation

import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.ImageOptions
import java.io.FileOutputStream

//int[][] rgbDataFrame1 = ...
//int[][] rgbDataFrame2 = ...
//int[][] rgbDataFrame3 = ...

fun hsutenssaft() {
    val width = 255
    val height = 255
    val rgbDataFrame1 = Array(height) { IntArray(width) }
    for (y in 0 until height) {
        for (x in 0 until width) {
            rgbDataFrame1[y][x] = x * y
        }
    }
    val outputStream = FileOutputStream("output/test.gif")
    val options = ImageOptions()
    GifEncoder(outputStream, width, height, 0)
        .addImage(rgbDataFrame1, options)
//        .addImage(rgbDataFrame2, options)
//        .addImage(rgbDataFrame3, options)
        .finishEncoding()
    outputStream.close()
}

fun main() {
    hsutenssaft()
}
