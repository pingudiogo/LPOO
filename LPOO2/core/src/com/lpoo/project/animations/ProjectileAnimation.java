package com.lpoo.project.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lpoo.project.logic.Game;
import com.lpoo.project.logic.Projectile.ProjectileStatus;

/**
 * Class that creates the projectile's animation
 * This class extends the class Animator
 */
public class ProjectileAnimation extends Animator {

    /**
     * Projectile's explosion's index
     */
    private static final int EXPLODE_INDEX = 0;
    /**
     * Size of the projectile's array
     */
    private static final int ARRAY_SIZE = 1;
    /**
     * Velocity of the projectile's explosion
     */
    private final float explode_speed = 1/10f;

    /**
     * Constructor for the ProjectileAnimator class
     * @param game Game where will be placed the animation
     * @param path Path where is saved the TextureAtlas of the projectile's movement
     * @param index Projectile's index
     */
    public ProjectileAnimation(Game game, String path, int index ) {
        super(game, ARRAY_SIZE, ARRAY_SIZE, index);

        textures[EXPLODE_INDEX] = new TextureAtlas(Gdx.files.internal(path));
        animations[EXPLODE_INDEX] = new Animation(explode_speed, textures[EXPLODE_INDEX].getRegions());
    }

    /**
     * Verifies if the animation already finished
     * @return True if the animation was already finished, False if not
     */
    public boolean isFinished() {
        return explode_speed * 4 <= stateTime;
    }
    /**
     * Getter for the animation's texture
     * @param delta Difference between the last time of call and the current time
     * @return TextureRegion to be drawn on the screen
     */
    public TextureRegion getTexture( float delta ) {
        if(  game.getProjectiles().get(index).getState() == ProjectileStatus.HIT_TARGET) {
            stateTime += delta;
            return animations[EXPLODE_INDEX].getKeyFrame(stateTime, false);
        }
        return animations[EXPLODE_INDEX].getKeyFrames()[0];
    }
}
