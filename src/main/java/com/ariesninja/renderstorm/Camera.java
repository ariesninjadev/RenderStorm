package com.ariesninja.renderstorm;

import org.joml.Matrix4f;

public class Camera {

    private boolean initialized = false;

    private float cDefX, cDefY, cDefZ;
    private float cDefPitch, cDefYaw;

    private float cameraX = 0.0f, cameraY = 0.0f, cameraZ = 0.0f;
    private float pitch = 0.0f, yaw = 0.0f;

    private float targetCameraX = 0.0f, targetCameraY = 0.0f, targetCameraZ = 5.0f;
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

    public void init(float cDefX, float cDefY, float cDefZ, float cDefPitch, float cDefYaw) {
        this.cDefX = cDefX;
        this.cDefY = cDefY;
        this.cDefZ = cDefZ;
        this.cDefPitch = cDefPitch;
        this.cDefYaw = cDefYaw;
        initialized = true;
        resetPositionInstant();
    }

    public void updateCamera(float deltaTime) {
        checkInitialized();
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

        // Handle yaw wrap-around
        float deltaYaw = targetYaw - yaw;
        if (deltaYaw > 180.0f) {
            deltaYaw -= 360.0f;
        } else if (deltaYaw < -180.0f) {
            deltaYaw += 360.0f;
        }
        yaw += deltaYaw * smoothingFactor;

        // Clamp pitch and targetPitch to prevent looking beyond straight up or down
        if (pitch < -90.0f) {
            pitch = -90.0f;
        } else if (pitch > 90.0f) {
            pitch = 90.0f;
        }

        if (targetPitch < -90.0f) {
            targetPitch = -90.0f;
        } else if (targetPitch > 90.0f) {
            targetPitch = 90.0f;
        }

        // Clamp yaw to be between -180 and 180 degrees
        if (yaw < -180.0f) {
            yaw += 360.0f;
        } else if (yaw > 180.0f) {
            yaw -= 360.0f;
        }

        if (targetYaw < -180.0f) {
            targetYaw += 360.0f;
        } else if (targetYaw > 180.0f) {
            targetYaw -= 360.0f;
        }
    }

    public void increaseFov() {
        targetFov = 75.0f; // Increase FOV
    }

    public void resetFov() {
        targetFov = 60.0f; // Reset FOV
    }

    public void moveForward(float deltaTime) {
        checkInitialized();
        targetCameraX += speed * deltaTime * Math.sin(Math.toRadians(targetYaw));
        targetCameraZ -= speed * deltaTime * Math.cos(Math.toRadians(targetYaw));
    }

    public void moveBackward(float deltaTime) {
        checkInitialized();
        targetCameraX -= speed * deltaTime * Math.sin(Math.toRadians(targetYaw));
        targetCameraZ += speed * deltaTime * Math.cos(Math.toRadians(targetYaw));
    }

    public void moveLeft(float deltaTime) {
        checkInitialized();
        targetCameraX -= speed * deltaTime * Math.cos(Math.toRadians(targetYaw));
        targetCameraZ -= speed * deltaTime * Math.sin(Math.toRadians(targetYaw));
    }

    public void moveRight(float deltaTime) {
        checkInitialized();
        targetCameraX += speed * deltaTime * Math.cos(Math.toRadians(targetYaw));
        targetCameraZ += speed * deltaTime * Math.sin(Math.toRadians(targetYaw));
    }

    public void moveUp(float deltaTime) {
        checkInitialized();
        targetCameraY += speed * deltaTime; // Vertical up
    }

    public void moveDown(float deltaTime) {
        checkInitialized();
        targetCameraY -= speed * deltaTime; // Vertical down
    }

    public void rotateUp(float deltaTime) {
        checkInitialized();
        targetPitch -= rotationSpeed * deltaTime;
    }

    public void rotateDown(float deltaTime) {
        checkInitialized();
        targetPitch += rotationSpeed * deltaTime;
    }

    public void rotateLeft(float deltaTime) {
        checkInitialized();
        targetYaw -= rotationSpeed * deltaTime;
    }

    public void rotateRight(float deltaTime) {
        checkInitialized();
        targetYaw += rotationSpeed * deltaTime;
    }

    public void resetPositionSmooth() {
        checkInitialized();
        targetCameraX = cDefX;
        targetCameraY = cDefY;
        targetCameraZ = cDefZ;
        targetPitch = cDefPitch;
        targetYaw = cDefYaw;
    }

    public void resetPositionInstant() {
        checkInitialized();
        cameraX = cDefX;
        cameraY = cDefY;
        cameraZ = cDefZ;
        pitch = cDefPitch;
        yaw = cDefYaw;
        targetCameraX = cDefX;
        targetCameraY = cDefY;
        targetCameraZ = cDefZ;
        targetPitch = cDefPitch;
        targetYaw = cDefYaw;
    }

    public float[] getViewMatrix() {
        checkInitialized();
        Matrix4f viewMatrix = new Matrix4f()
                .rotateX((float) Math.toRadians(pitch))
                .rotateY((float) Math.toRadians(yaw))
                .translate(-cameraX, -cameraY, -cameraZ);
        float[] viewArray = new float[16];
        viewMatrix.get(viewArray);
        return viewArray;
    }

    public float[] getProjectionMatrix() {
        checkInitialized();
        Matrix4f projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(fov), (float) width / (float) height, 0.1f, 100.0f);
        float[] projectionArray = new float[16];
        projectionMatrix.get(projectionArray);
        return projectionArray;
    }

    public float getCameraX() {
        checkInitialized();
        return cameraX;
    }

    public float getCameraY() {
        checkInitialized();
        return cameraY;
    }

    public float getCameraZ() {
        checkInitialized();
        return cameraZ;
    }

    public float getPitchDeg() {
        checkInitialized();
        if (pitch == 0.0f) {
            return 0.0f;
        }
        return -pitch;
    }

    public float getYawDeg() {
        checkInitialized();
        return yaw;
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Camera is not initialized");
        }
    }
}