package io.github.ocirne.ray.bewegt.canvas

import java.io.File

class PPM(val filename: String) {

    fun writeToFile(frame: RgbDataFrame) {
        File(filename).printWriter().use { out ->
            out.println("P3")
            out.println("${frame.width} ${frame.height}")
            out.println("255")
            for (y in frame.height - 1 downTo 0) {
                for (x in 0 until frame.width) {
                    out.println(frame.atAsString(x, y))
                }
            }
        }
    }
}
