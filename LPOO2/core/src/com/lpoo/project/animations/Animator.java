package com.lpoo.project.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.lpoo.project.logic.Game;

/**
 * Class that creates the animations
 * This class implements the interface Disposable and Cloneable
 */
public abstract class Animator implements Disposable, Cloneable {

    /**
     * Time given to the animation's life;
     */
    protected float stateTime;

    /**
     * Array with all animations
     */
    protected Animation[] animations;

    /**
     * Array with all TextureAtlas
     */
    protected TextureAtlas[] textures;

    /**
     * Game where will be placed the animations
     */
    protected Game game;

    /**
     * Animation's index
     */
    protected int index;

    /**
     * Constructor for the class Animation
     * @param game Game where will be placed the animations
     * @param nAnimations Number of animations
     * @param nTextures Number of textures
     * @param index Animation's index
     */
    public Animator( Game game, int nAnimations, int nTextures, int index ) {
        this.game = game;
        animations = new Animation[nAnimations];
        textures = new TextureAtlas[nTextures];
        stateTime = 0;
        this.index = index;
    }


    /**
     * Getter for the animations
     * @return The array with the animations
     */
    public Animation[] getAnimations() {
        return animations;
    }

    /**
     * Getter for the animation's texture
     * @param delta Difference between the last time of call and the current time
     * @return TextureRegion to be drawn on the screen
     */
    public TextureRegion getTexture( float delta ) {
        stateTime += delta;
        return animations[0].getKeyFrame( delta, true );
    }

    /**
     * Setter for the animation's index
     * @param index Animation's index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Verifies if the animation already finished
     * @return True if the animation was already finished, False if not
     */
    public boolean isFinished() {
        return false;
    }

    @Override
    /**
     * Releases all textures of the animation
     */
    public void dispose() {
        for( int i = 0; i < textures.length; i++ )
            textures[i].dispose();
    }

    /**
     * Resets the animations
     * @param game Game where the animations will be placed
     * @param index Animation's index
     */
    public void reset( Game game, int index ) {
        stateTime = 0;
        this.game = game;
        this.index = index;
    }

    @Override

    /**
     * Clones the animation
     */
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
