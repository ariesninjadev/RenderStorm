package com.ariesninja.renderstorm;

import org.joml.Matrix4f;

public class Camera {
    private float cameraX = 0.0f, cameraY = 0.0f, cameraZ = 5.0f;
    private float targetCameraX = 0.0f, targetCameraY = 0.0f, targetCameraZ = 5.0f;
    private float pitch = 0.0f, yaw = 0.0f;
    private float targetPitch = 0.0f, targetYaw = 0.0f;
    private final float speed = 0.04f;          // Movement speed
    private final float rotationSpeed = 0.32f;   // Rotation speed

    private int width, height;

    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void updateCamera() {
        // Interpolate position
        // Smoothing factor
        float smoothingFactor = 0.1f;
        cameraX += (targetCameraX - cameraX) * smoothingFactor;
        cameraY += (targetCameraY - cameraY) * smoothingFactor;
        cameraZ += (targetCameraZ - cameraZ) * smoothingFactor;

        // Interpolate rotation
        pitch += (targetPitch - pitch) * smoothingFactor;
        yaw += (targetYaw - yaw) * smoothingFactor;
    }

    public void moveForward() {
        targetCameraX += speed * Math.sin(Math.toRadians(targetYaw));
        targetCameraZ -= speed * Math.cos(Math.toRadians(targetYaw));
    }

    public void moveBackward() {
        targetCameraX -= speed * Math.sin(Math.toRadians(targetYaw));
        targetCameraZ += speed * Math.cos(Math.toRadians(targetYaw));
    }

    public void moveLeft() {
        targetCameraX -= speed * Math.cos(Math.toRadians(targetYaw));
        targetCameraZ -= speed * Math.sin(Math.toRadians(targetYaw));
    }

    public void moveRight() {
        targetCameraX += speed * Math.cos(Math.toRadians(targetYaw));
        targetCameraZ += speed * Math.sin(Math.toRadians(targetYaw));
    }

    public void moveUp() {
        targetCameraY += speed; // Vertical up
    }

    public void moveDown() {
        targetCameraY -= speed; // Vertical down
    }

    public void rotateUp() {
        targetPitch -= rotationSpeed;
    }

    public void rotateDown() {
        targetPitch += rotationSpeed;
    }

    public void rotateLeft() {
        targetYaw -= rotationSpeed;
    }

    public void rotateRight() {
        targetYaw += rotationSpeed;
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

    public float getPitchDeg() {
        return -pitch;
    }

    public float getYawDeg() {
        return yaw;
    }
}