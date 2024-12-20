package com.ariesninja.renderstorm;

import org.joml.Matrix4f;

public class Camera {
    private float cameraX = 0.0f, cameraY = 0.0f, cameraZ = 5.0f;
    private float targetCameraX = 0.0f, targetCameraY = 0.0f, targetCameraZ = 5.0f;
    private float pitch = 0.0f, yaw = 0.0f;
    private float targetPitch = 0.0f, targetYaw = 0.0f;
    private final float speed = 4.0f;          // Movement speed
    private final float rotationSpeed = 36.0f;   // Rotation speed
    private float fov = 60.0f; // Field of view
    private float targetFov = 60.0f; // Target field of view

    private final int width;
    private final int height;

    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void updateCamera(float deltaTime) {
        // Interpolate position
        float smoothingFactor = 0.1f;
        cameraX += (targetCameraX - cameraX) * smoothingFactor;
        cameraY += (targetCameraY - cameraY) * smoothingFactor;
        cameraZ += (targetCameraZ - cameraZ) * smoothingFactor;

        // Interpolate rotation
        pitch += (targetPitch - pitch) * smoothingFactor;
        yaw += (targetYaw - yaw) * smoothingFactor;

        // Interpolate FOV
        fov += (targetFov - fov) * smoothingFactor;
    }

    public void increaseFov() {
        targetFov = 75.0f; // Increase FOV
    }

    public void resetFov() {
        targetFov = 60.0f; // Reset FOV
    }

    public void moveForward(float deltaTime) {
        targetCameraX += speed * deltaTime * Math.sin(Math.toRadians(targetYaw));
        targetCameraZ -= speed * deltaTime * Math.cos(Math.toRadians(targetYaw));
    }

    public void moveBackward(float deltaTime) {
        targetCameraX -= speed * deltaTime * Math.sin(Math.toRadians(targetYaw));
        targetCameraZ += speed * deltaTime * Math.cos(Math.toRadians(targetYaw));
    }

    public void moveLeft(float deltaTime) {
        targetCameraX -= speed * deltaTime * Math.cos(Math.toRadians(targetYaw));
        targetCameraZ -= speed * deltaTime * Math.sin(Math.toRadians(targetYaw));
    }

    public void moveRight(float deltaTime) {
        targetCameraX += speed * deltaTime * Math.cos(Math.toRadians(targetYaw));
        targetCameraZ += speed * deltaTime * Math.sin(Math.toRadians(targetYaw));
    }

    public void moveUp(float deltaTime) {
        targetCameraY += speed * deltaTime; // Vertical up
    }

    public void moveDown(float deltaTime) {
        targetCameraY -= speed * deltaTime; // Vertical down
    }

    public void rotateUp(float deltaTime) {
        targetPitch -= rotationSpeed * deltaTime;
    }

    public void rotateDown(float deltaTime) {
        targetPitch += rotationSpeed * deltaTime;
    }

    public void rotateLeft(float deltaTime) {
        targetYaw -= rotationSpeed * deltaTime;
    }

    public void rotateRight(float deltaTime) {
        targetYaw += rotationSpeed * deltaTime;
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
        Matrix4f projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(fov), (float) width / (float) height, 0.1f, 100.0f);
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

    public float getPitchDeg() {
        return -pitch;
    }

    public float getYawDeg() {
        return yaw;
    }
}