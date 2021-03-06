package com.lpoo.project.logic;

/**
 * Class that creates the game's characters
 * This class extends the superclass Entity and it implements the Updatable, Movable and Hitable interfaces
 */
public class Character extends Entity implements Updatable, Hitable, Movable {

    /**
     * Maximum velocity per second
     */
    protected static final int max_velocity = 20;

    /**
     * Character's properties
     */
    protected CharacterStats stats;

    /**
     * Constructor for the class Constructor
     * @param game Game where the character will be placed
     * @param x Character's x position
     * @param y Character's y position
     * @param width Character's width
     * @param height Character's height
     * @param stats Character's properties
     */
    public Character(Game game, int x, int y, int width, int height, CharacterStats stats) {
        super(game, x, y, width, height);
        this.stats = stats;
    }

    /**
     * Constructor for the class Constructor
     * @param game Game where the character will be placed
     * @param x Character's x position
     * @param y Character's y position
     * @param width Character's width
     * @param height Character's height
     */
    public Character(Game game, int x, int y, int width, int height) {
        super(game, x, y, width, height);
    }

    /**
     * Getter for stats
     * @return character's current stats
     */
    public CharacterStats getStats() {
        return stats;
    }

    /**
     * Getter for velocity
     * @return character's velocity
     */
    public float getVelocity() {
        return stats.getMovSpeed();
    }

    /**
     * Updates the enemy and current status
     * @param delta Difference between the last time of call and the current time
     */
    public void update(float delta) { }

    /**
     * Verifies if the enemy was hit by a projectile and if its life is 0 or less the enemy dies
     * @param stats Enemy's properties
     */
    public void hit(Stats stats) { }

    /**
     * Allows the hero to move
     * @param dir Movement's direction
     * @param delta Difference between the last time of call and the current time
     */
    public void move( int dir, float delta ) {
        rect.x += getVelocity() * delta * dir;
    }
}
