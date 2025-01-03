package com.ariesninja.renderstorm;

import static org.lwjgl.opengl.GL45.*;

public class Cube {

    private int vao, vbo, ebo, textureID;
    private float[] vertices = {
            // Positions          // Texture Coords
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // Back face
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, // Front face
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // Left face
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 0.0f, // Right face
            0.5f,  0.5f, -0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, // Top face
            0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, // Bottom face
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f
    };

    private int[] indices = {
            0, 1, 2, 2, 3, 0,   // Front face
            4, 5, 6, 6, 7, 4,   // Back face
            8, 9, 10, 10, 11, 8, // Left face
            12, 13, 14, 14, 15, 12, // Right face
            16, 17, 18, 18, 19, 16, // Top face
            20, 21, 22, 22, 23, 20  // Bottom face
    };

    private float coordX, coordY, coordZ;

    public Cube(int texture, float coordX, float coordY, float coordZ) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.coordZ = coordZ;
        textureID = TextureLoader.getGlobal().loadTexture(texture);
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

    public int getVao() {
        return vao;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getCoordX() {
        return coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public float getCoordZ() {
        return coordZ;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVertexCount() {
        return indices.length;
    }
}