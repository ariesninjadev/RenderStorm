package com.ariesninja.renderstorm;

import org.joml.Matrix4f;

public class Camera {
    private float cameraX = 0.0f, cameraY = 0.0f, cameraZ = 5.0f;
    private float pitch = 0.0f, yaw = 0.0f;
    private float speed = 0.07f;          // Movement speed
    private float rotationSpeed = 0.4f;   // Rotation speed

    private int width, height;

    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void updateCamera() {
        // Update camera logic if needed
    }

    public void moveForward() {
        cameraX += speed * Math.sin(Math.toRadians(yaw));
        cameraZ -= speed * Math.cos(Math.toRadians(yaw));
    }

    public void moveBackward() {
        cameraX -= speed * Math.sin(Math.toRadians(yaw));
        cameraZ += speed * Math.cos(Math.toRadians(yaw));
    }

    public void moveLeft() {
        cameraX -= speed * Math.cos(Math.toRadians(yaw));
        cameraZ -= speed * Math.sin(Math.toRadians(yaw));
    }

    public void moveRight() {
        cameraX += speed * Math.cos(Math.toRadians(yaw));
        cameraZ += speed * Math.sin(Math.toRadians(yaw));
    }

    public void moveUp() {
        cameraY += speed; // Vertical up
    }

    public void moveDown() {
        cameraY -= speed; // Vertical down
    }

    public void rotateUp() {
        pitch -= rotationSpeed;
    }

    public void rotateDown() {
        pitch += rotationSpeed;
    }

    public void rotateLeft() {
        yaw -= rotationSpeed;
    }

    public void rotateRight() {
        yaw += rotationSpeed;
    }

    public float[] getViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f()
                .rotateX((float) Math.toRadians(pitch))
                .rotateY((float) Math.toRadians(yaw))
                .translate(-cameraX, -cameraY, -cameraZ);
        float[] viewArray = new float[16];
        viewMatrix.get(viewArray);
        return viewArray;
    }

    public float[] getProjectionMatrix() {
        Matrix4f projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) width / (float) height, 0.1f, 100.0f);
        float[] projectionArray = new float[16];
        projectionMatrix.get(projectionArray);
        return projectionArray;
    }

    public float getCameraX() {
        return cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }

    public float getCameraZ() {
        return cameraZ;
    }
}