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
    private World world;
    private Cube cube;
    private Camera camera;
    private InputHandler inputHandler;
    private Shader shader;
    private TextWindow textWindow;

    private int screenWidth = 2560/2;
    private int screenHeight = 1440/2;

    private float deltaTime;
    private long lastFrameTime;

    private long lastTime;
    private int frames;
    private float fps;

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

        // Get the base directory
        String baseDir = System.getProperty("user.dir");

        // Initialize components with relative paths
        shader = new Shader(
                baseDir + "/src/main/resources/data/vertex_shader.glsl",
                baseDir + "/src/main/resources/data/fragment_shader.glsl"
        );

        // Initialize the world
        world = new World();

        cube = new Cube(baseDir + "/src/main/resources/4krender.png", shader, 0.0f, 0.0f, 0.0f);
        world.addCube(cube);

        // Make a 20x20 ground made of grass
        for (int i = -10; i < 10; i++) {
            for (int j = -10; j < 10; j++) {
                world.addCube(new Cube(baseDir + "/src/main/resources/grass.png", shader, i, -3.0f, j));
            }
        }

        camera = new Camera(screenWidth, screenHeight);
        inputHandler = new InputHandler(window, camera);

        // Initialize TextWindow
        textWindow = new TextWindow();
        SwingUtilities.invokeLater(() -> textWindow.setVisible(true));

        // Initialize FPS variables
        lastTime = System.currentTimeMillis();
        frames = 0;
        fps = 0.0f;
    }

    private void loop() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glEnable(GL_DEPTH_TEST);

        lastFrameTime = System.currentTimeMillis();

        while (!glfwWindowShouldClose(window)) {
            long currentFrameTime = System.currentTimeMillis();
            deltaTime = (currentFrameTime - lastFrameTime) / 1000.0f;
            lastFrameTime = currentFrameTime;

            inputHandler.handleInput(deltaTime);
            camera.updateCamera(deltaTime);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Render the world
            for (Cube cube : world.getCubes()) {
                cube.renderCube(camera);
            }

            // Add Camera Data
            textWindow.add(String.format("Cam:\nX %.2f  Y %.2f  Z %.2f\nY %.2f  P %.2f", camera.getCameraX(), camera.getCameraY(), camera.getCameraZ(),  camera.getYawDeg(), camera.getPitchDeg()));
            textWindow.blank();

            // Calculate FPS
            frames++;
            if (currentFrameTime - lastTime >= 1000) {
                fps = frames * 1000.0f / (currentFrameTime - lastTime);
                lastTime = currentFrameTime;
                frames = 0;
            }

            // Add FPS Data
            textWindow.add(String.format("FPS: %.2f", fps));

            SwingUtilities.invokeLater(() -> textWindow.post());

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