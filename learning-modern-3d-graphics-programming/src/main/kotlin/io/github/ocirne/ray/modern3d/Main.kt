package io.github.ocirne.ray.modern3d

import org.lwjgl.BufferUtils
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30C.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memAllocFloat

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
        window = GLFW.glfwCreateWindow(300, 300, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL)
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

    private fun loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        glViewport(0, 0, 300, 300)

        val theProgram = initializeProgram()

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!GLFW.glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

            glUseProgram(theProgram)

            val positionBufferObject = initializeVertexBuffer()

            glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject)
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);

            glDrawArrays(GL_TRIANGLES, 0, 3)

            glDisableVertexAttribArray(0)
            glUseProgram(0);

            glfwSwapBuffers(window) // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
        }
    }

    fun initializeVertexBuffer(): Int {
        val vertexPositions = BufferUtils.createFloatBuffer(12)
        vertexPositions.put(0, 0.75f)
        vertexPositions.put(1, 0.75f)
        vertexPositions.put(2, 0.0f)
        vertexPositions.put(3, 1.0f)
        vertexPositions.put(4, 0.75f)
        vertexPositions.put(5, -0.75f)
        vertexPositions.put(6, 0.0f)
        vertexPositions.put(7, 1.0f)
        vertexPositions.put(8, -0.75f)
        vertexPositions.put(9, -0.75f)
        vertexPositions.put(10, 0.0f)
        vertexPositions.put(11, 1.0f)

        val positionBufferObject = glGenBuffers()

        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject)
        glBufferData(GL_ARRAY_BUFFER, vertexPositions, GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        return positionBufferObject
    }

    val strVertexShader = """
        #version 330

        layout(location = 0) in vec4 position;
        void main(void)
        {
            gl_Position = position;
        }
        """

    val strFragmentShader = """
        #version 330

        out vec4 outputColor;
        void main(void)
        {
           outputColor = vec4(1.0f, 1.0f, 1.0f, 0.5f);
        }
        """

    fun initializeProgram(): Int {
        val shaderList = listOf(
            createShader(GL_VERTEX_SHADER, strVertexShader),
            createShader(GL_FRAGMENT_SHADER, strFragmentShader)
        )
        val theProgram = createProgram(shaderList)

        for (shader in shaderList) {
            glDeleteShader(shader)
        }
        return theProgram
    }

    fun createShader(eShaderType: Int, strShader: String): Int {
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
}

fun main() {
    HelloWorld().run()
}