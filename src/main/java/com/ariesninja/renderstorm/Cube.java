package com.ariesninja.renderstorm;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL45.*;

public class Cube {
    private int vao, vbo, ebo, textureID;
    private Shader shader;
    private float[] vertices = {
            // Positions          // Texture Coords
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, // 0
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f, // 1
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f, // 2
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, // 3
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, // 4
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f, // 5
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f, // 6
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f  // 7
    };

    private int[] indices = {
            0, 1, 2, 2, 3, 0,   // Front face
            4, 5, 6, 6, 7, 4,   // Back face
            4, 5, 1, 1, 0, 4,   // Bottom face
            6, 7, 3, 3, 2, 6,   // Top face
            7, 4, 0, 0, 3, 7,   // Left face
            1, 5, 6, 6, 2, 1    // Right face
    };

    public Cube(String texturePath, Shader shader) {
        this.shader = shader;
        textureID = TextureLoader.loadTexture(texturePath);
        initCube();
    }

    private void initCube() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void renderCube(Camera camera) {
        shader.use();
        shader.setMat4("view", camera.getViewMatrix());
        shader.setMat4("projection", camera.getProjectionMatrix());

        Matrix4f model = new Matrix4f().identity();
        float[] modelArray = new float[16];
        model.get(modelArray);
        shader.setMat4("model", modelArray);

        glBindTexture(GL_TEXTURE_2D, textureID);
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}