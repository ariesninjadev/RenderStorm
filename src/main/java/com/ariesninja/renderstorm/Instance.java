package com.ariesninja.renderstorm;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import javax.swing.*;
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
    private TextWindow textWindow;

    private int screenWidth = 2560/2;
    private int screenHeight = 1440/2;

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

        window = glfwCreateWindow(screenWidth, screenHeight, "RenderStorm", 0, 0);
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
        cube = new Cube("C:\\Users\\aries\\Documents\\Development\\RenderStorm\\src\\main\\resources\\4krender.png", shader);
        camera = new Camera(screenWidth, screenHeight);
        inputHandler = new InputHandler(window, camera);

        // Initialize TextWindow
        textWindow = new TextWindow();
        SwingUtilities.invokeLater(() -> textWindow.setVisible(true));

    }

    private void loop() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glEnable(GL_DEPTH_TEST);

        while (!glfwWindowShouldClose(window)) {
            inputHandler.handleInput();
            camera.updateCamera();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            cube.renderCube(camera);

            String cameraInfo = String.format("Camera Position: (%.2f, %.2f, %.2f)", camera.getCameraX(), camera.getCameraY(), camera.getCameraZ());
            SwingUtilities.invokeLater(() -> textWindow.updateText(cameraInfo));

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