package com.ariesninja.renderstorm;

import org.joml.Matrix4f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL45.*;

public class World {

    private ArrayList<Cube> cubes;
    private Shader shader;

    public World(Shader shader) {
        cubes = new ArrayList<>();
        this.shader = shader;
    }

    public void addCube(Cube cube) {
        cubes.add(cube);
    }

    public ArrayList<Cube> getCubes() {
        return cubes;
    }

    public void render(Camera camera) {
        shader.use();
        shader.setMat4("view", camera.getViewMatrix());
        shader.setMat4("projection", camera.getProjectionMatrix());

        for (Cube cube : cubes) {
            Matrix4f model = new Matrix4f().identity().translate(cube.getCoordX() + 0.5f, cube.getCoordY() + 0.5f, cube.getCoordZ() + 0.5f);
            float[] modelArray = new float[16];
            model.get(modelArray);
            shader.setMat4("model", modelArray);

            glBindTexture(GL_TEXTURE_2D, cube.getTextureID());
            glBindVertexArray(cube.getVao());
            glDrawElements(GL_TRIANGLES, cube.getIndices().length, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
        }
    }
}