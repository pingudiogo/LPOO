package com.lpoo.project.logic;


/**
 * Created by Vasco on 12/05/2016.
 */
public class Enemy extends Character implements Updatable, Movable, Hitable {

    public enum EnemyStatus { ATTACK, MOVE_RIGHT, DEAD }
    private EnemyStatus state, nextState;
    private float stateTime;

    private float move_speed, attack_speed, attack_time;
    private float velocity;
    private boolean attacked;

    /**
     * @brief Constructor for the class Enemy
     * @param x
     * @param y
     * @param health
     * @param resistance
     * @param strength
     */
    public Enemy( Game game, int x, int y, int health, int resistance, int strength )  {
        super( game, x, y, 80, 124, health, resistance, strength );
        stateTime = 0;
        state = EnemyStatus.MOVE_RIGHT;
        nextState = EnemyStatus.MOVE_RIGHT;
        move_speed = 1/4f;
        attack_speed = 1.4f;
        attack_time = 0.6f;
        attacked = false;
        velocity = 40f;
    }

    public EnemyStatus getState() {
        return state;
    }

    public EnemyStatus getNextState () {
        return nextState;
    }

    public void AnimationStatus( EnemyStatus stat ) {
        if( stat != state ) {
            nextState = stat;
            state = stat;
            stateTime = 0;
        }
    }

    @Override
    public void update(float delta) {
        stateTime += delta;

        switch( state ) {
            case DEAD:
                break;
            case MOVE_RIGHT:
                move( 1, delta );
                break;
            case ATTACK:
                if( stateTime >= attack_speed ) {
                    stateTime -= attack_speed;
                    attacked = false;
                } else if( stateTime >= attack_time && !attacked ) {
                    attacked = true;
                    game.getHero().hit(stats.getStrength());
                }
                break;
        }

        //Change state
        if( state != EnemyStatus.DEAD /* && stateTime >= getSpeed( state )*/ ) {
            if ( rect.overlaps( game.getHero().getRect()) )
                nextState = EnemyStatus.ATTACK;
            else
                nextState = EnemyStatus.MOVE_RIGHT;
        }
    }

    public void hit( int damage ) {
        stats.setHealth(damage);
        if(stats.getHealth()<=0) {
            state = EnemyStatus.DEAD;
            nextState = EnemyStatus.DEAD;
        }
    }

    @Override
    public void move(int dir, float delta) {
        rect.x += velocity * dir * delta;
    }
}
