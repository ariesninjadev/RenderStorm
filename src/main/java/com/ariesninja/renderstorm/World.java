package com.ariesninja.renderstorm;

// TEMPORARY! REMOVE THIS LATER

import java.util.ArrayList;

public class World {

    private ArrayList<Cube> cubes;

    public World() {
        cubes = new ArrayList<>();
    }

    public void addCube(Cube cube) {
        cubes.add(cube);
    }

    public ArrayList<Cube> getCubes() {
        return cubes;
    }

}
