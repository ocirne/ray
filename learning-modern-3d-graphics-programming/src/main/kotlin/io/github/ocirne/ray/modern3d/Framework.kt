package io.github.ocirne.ray.modern3d

interface Framework {

    /** called before OpenGL is initialized */
//    fun defaults()

    /** after OpenGL is initialized */
//    fun initialization()

    /** called when the screen needs some rendering */
    fun display()

    /** when the window is resized */
    fun reshape(w: Int, h: Int)

    /** called when the user presses a key */
//    fun keyboard()
}