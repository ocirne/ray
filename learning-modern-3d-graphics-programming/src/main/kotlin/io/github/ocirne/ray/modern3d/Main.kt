package io.github.ocirne.ray.modern3d

import org.lwjgl.BufferUtils
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30C.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class HelloWorld {

    // The window handle
    private var window: Long = 0

    fun run() {
        println("Hello LWJGL " + Version.getVersion() + "!")
        init()
        loop()

        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(window)
        GLFW.glfwDestroyWindow(window)

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)!!.free()
    }

    private fun init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE) // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE) // the window will be resizable

        // Create the window
        window = GLFW.glfwCreateWindow(500, 500, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        GLFW.glfwSetKeyCallback(
            window
        ) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) GLFW.glfwSetWindowShouldClose(
                window,
                true
            ) // We will detect this in the rendering loop
        }
        MemoryStack.stackPush().let { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())

            // Center the window
            GLFW.glfwSetWindowPos(
                window,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window)
        // Enable v-sync
        GLFW.glfwSwapInterval(1)

        // Make the window visible
        GLFW.glfwShowWindow(window)
    }

    fun reshape(w: Int, h: Int) {
        glViewport(0, 0, w, h)
    }

    private fun loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        val theProgram = initializeProgram()
        val offsetLocation = glGetUniformLocation(theProgram, "offset")

        val positionBufferObject = initializeVertexBuffer()

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            handleResize()

            val (fXOffset, fYOffset) = computePositionOffsets()
            adjustVertexData(fXOffset, fYOffset)

            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

            glUseProgram(theProgram)

            glUniform2f(offsetLocation, fXOffset, fYOffset)

            glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject)
            glEnableVertexAttribArray(0)
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0)
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 48)

            glDrawArrays(GL_TRIANGLES, 0, 3)

            glDisableVertexAttribArray(0)
            glDisableVertexAttribArray(1)
            glUseProgram(0);

            glfwSwapBuffers(window) // swap the color buffers

//            glutPostRedisplay()  ??

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
        }
    }

    fun handleResize() {
        stackPush().let { stack ->
            val fw: IntBuffer = stack.mallocInt(1)
            val fh: IntBuffer = stack.mallocInt(1)

            glfwGetFramebufferSize(window, fw, fh)
            val framebufferWidth = fw[0]
            val framebufferHeight = fh[0]

            val framebufferSize = min(framebufferWidth, framebufferHeight)

            glViewport(
                (framebufferWidth - framebufferSize) / 2,
                (framebufferHeight - framebufferSize) / 2,
                framebufferSize,
                framebufferSize)
        }
    }

    var vertexPositions: FloatArray = floatArrayOf()
    var positionBufferObject: Int = 0

    fun initializeVertexBuffer(): Int {
        vertexPositions = floatArrayOf(
            0.0f,    0.5f, 0.0f, 1.0f,
            0.5f, -0.366f, 0.0f, 1.0f,
            -0.5f, -0.366f, 0.0f, 1.0f,
            1.0f,    0.0f, 0.0f, 1.0f,
            0.0f,    1.0f, 0.0f, 1.0f,
            0.0f,    0.0f, 1.0f, 1.0f,
        )
        positionBufferObject = glGenBuffers()

        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject)
        glBufferData(GL_ARRAY_BUFFER, vertexPositions, GL_STREAM_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        return positionBufferObject
    }

    fun initializeProgram(): Int {
        val shaderList = listOf(
            loadShader(GL_VERTEX_SHADER, "OffsettingShader.vert"),
            loadShader(GL_FRAGMENT_SHADER, "FragPosition.frag")
        )
        val theProgram = createProgram(shaderList)

        for (shader in shaderList) {
            glDeleteShader(shader)
        }
        return theProgram
    }

    fun loadShader(eShaderType: Int, shaderFilename: String): Int {
        val strShader = HelloWorld::class.java.classLoader.getResource(shaderFilename)!!.readText()
        val shader = glCreateShader(eShaderType)

        glShaderSource(shader, strShader)

        glCompileShader(shader)

        val status = glGetShaderi(shader, GL_COMPILE_STATUS)
        if (status == GL_FALSE) {
            val strInfoLog = glGetShaderInfoLog(shader)
            val strShaderType = when (eShaderType) {
                GL_VERTEX_SHADER -> "vertex"
//                GL_GEOMETRY_SHADER -> "geometry"
                GL_FRAGMENT_SHADER -> "fragment"
                else -> throw IllegalArgumentException()
            }
            println("Compile failure in $strShaderType shader:\n$strInfoLog\n")
        }
        return shader
    }

    fun createProgram(shaderList: List<Int>): Int {
        val program = glCreateProgram()

        for (shader in shaderList) {
            glAttachShader(program, shader)
        }

        glLinkProgram(program)

        val status = glGetProgrami(program, GL_LINK_STATUS)
        if (status == GL_FALSE) {
            val strInfoLog = glGetProgramInfoLog(program)
            println("Linker failure: $strInfoLog\n")
        }
        for (shader in shaderList) {
            glDetachShader(program, shader)
        }
        return program
    }

    val start = System.currentTimeMillis()

    fun computePositionOffsets(): Pair<Float, Float> {
        val fLoopDuration = 5
        val fScale = 3.14159f * 2.0f / fLoopDuration

        val fElapsedTime = (System.currentTimeMillis() - start) / 1000.0f

        val fCurrTimeThroughLoop = fElapsedTime % fLoopDuration

        val fXOffset = cos(fCurrTimeThroughLoop * fScale) * 0.5f
        val fYOffset = sin(fCurrTimeThroughLoop * fScale) * 0.5f

        return Pair(fXOffset, fYOffset)
    }

    fun adjustVertexData(fXOffset: Float, fYOffset: Float) {
        val fNewData = vertexPositions.copyOf()

        for (iVertex in vertexPositions.indices step 4) {
            fNewData[iVertex] += fXOffset
            fNewData[iVertex + 1] += fYOffset
        }

        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject)
        glBufferSubData(GL_ARRAY_BUFFER, 0, fNewData)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }
}

fun main() {
    HelloWorld().run()
}