package io.github.ocirne.ray.challenge.canvas

class Feedback(private val totalPixels: Int) {

    private var nextHop = 0

    private val startTime = System.currentTimeMillis()

    fun update(currentPixels: Int) {
        if (currentPixels >= nextHop) {
            val currentTime = System.currentTimeMillis() - startTime
            val percentage = 100 * currentPixels / totalPixels
            val totalTime = currentTime * totalPixels / currentPixels
            val restTime = totalTime - currentTime
            println("$percentage % ($currentPixels of $totalPixels) - ready in ${restTime/1000} seconds (total ${totalTime/1000} seconds)")
            nextHop = (percentage + 1) * totalPixels / 100

        }
    }
}