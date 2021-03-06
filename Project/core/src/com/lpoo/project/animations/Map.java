package com.lpoo.project.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * Class that creates the map
 * This class implements the interface Disposable
 */
public class Map implements Disposable {

    /**
     * Texture which represents the map
     */
    private Texture map;

    /**
     * Texture that represents the spawn wall
     */
    private Texture spawnWall;

    /**
     * Constructor for the Map class
     */
    public Map() {
        map = new Texture("Map\\Map.png");
        spawnWall = new Texture("Map\\SpawnWall.png");
    }

    /**
     * Getter for the map's texture
     * @return The map's texture
     */
    public final Texture getMap() {
        return map;
    }

    /**
     * Getter for the spawn wall's texture
     * @return The spawn wall's texture
     */
    public final Texture getSpawnWall() {
        return spawnWall;
    }

    @Override
    /**
     * Releases all textures of the map
     */
    public void dispose() {
        map.dispose();
        spawnWall.dispose();
    }
}
