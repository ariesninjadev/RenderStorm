package com.ariesninja.renderstorm;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryStack.*;

public class Instance {

    private long window;
    private Cube cube;
    private Camera camera;
    private InputHandler inputHandler;
    private Shader shader;

    public void run() throws IOException {
        init();
        loop();
        cleanup();
    }

    private void init() throws IOException {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 600, "Cube Renderer", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable v-sync
        glfwShowWindow(window);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);

        // Initialize components
        shader = new Shader(
                "C:\\Users\\aries\\Documents\\Development\\RenderStorm\\src\\main\\resources\\data\\vertex_shader.glsl",
                "C:\\Users\\aries\\Documents\\Development\\RenderStorm\\src\\main\\resources\\data\\fragment_shader.glsl"
        );
        cube = new Cube("C:\\Users\\aries\\Documents\\Development\\RenderStorm\\src\\main\\resources\\a.png", shader);
        camera = new Camera();
        inputHandler = new InputHandler(window, camera);
    }

    private void loop() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glEnable(GL_DEPTH_TEST);

        while (!glfwWindowShouldClose(window)) {
            inputHandler.handleInput();
            camera.updateCamera();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            cube.renderCube(camera);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}