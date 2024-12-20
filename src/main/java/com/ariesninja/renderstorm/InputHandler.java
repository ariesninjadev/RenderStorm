package com.ariesninja.renderstorm;

import org.lwjgl.glfw.GLFW;

public class InputHandler {
    private long window;
    private Camera camera;
    private boolean isShiftPressed = false;
    private boolean isCameraMoving = false;

    public InputHandler(long window, Camera camera) {
        this.window = window;
        this.camera = camera;
    }

    public void handleInput(float deltaTime) {
        isCameraMoving = false;

        // Check if SHIFT is pressed
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS) {
            isShiftPressed = true;
        } else {
            isShiftPressed = false;
        }

        float speedMultiplier = isShiftPressed ? 4.0f : 1.0f;

        // Movement
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            camera.moveForward(deltaTime * speedMultiplier);
            isCameraMoving = true;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            camera.moveBackward(deltaTime * speedMultiplier);
            isCameraMoving = true;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            camera.moveLeft(deltaTime * speedMultiplier);
            isCameraMoving = true;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            camera.moveRight(deltaTime * speedMultiplier);
            isCameraMoving = true;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
            camera.moveUp(deltaTime * speedMultiplier);
            isCameraMoving = true;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
            camera.moveDown(deltaTime * speedMultiplier);
            isCameraMoving = true;
        }

        // Rotation
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
            camera.rotateUp(deltaTime);
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
            camera.rotateDown(deltaTime);
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            camera.rotateLeft(deltaTime);
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            camera.rotateRight(deltaTime);
        }

        // Adjust FOV based on movement
        if (isShiftPressed && isCameraMoving) {
            camera.increaseFov();
        } else {
            camera.resetFov();
        }
    }
}