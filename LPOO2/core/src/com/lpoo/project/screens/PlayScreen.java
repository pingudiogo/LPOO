package com.lpoo.project.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lpoo.project.MyGame;
import com.lpoo.project.animations.EnemyAnimation;
import com.lpoo.project.animations.HeroAnimation;
import com.lpoo.project.animations.LifeBar;
import com.lpoo.project.animations.Map;
import com.lpoo.project.animations.ProjectileAnimation;
import com.lpoo.project.animations.TrapAnimation;
import com.lpoo.project.logic.Enemy;
import com.lpoo.project.logic.Game;
import com.lpoo.project.logic.Projectile;
import com.lpoo.project.logic.Trap;

import java.util.LinkedList;

/**
 * Created by Vasco on 13/05/2016.
 */
public class PlayScreen implements Screen {

    private OrthographicCamera camera;
    private MyGame myGame;
    public Game game;
    private BitmapFont font;

    private HeroAnimation hero_animations;
    private LinkedList<EnemyAnimation> enemies;
    private TrapAnimation[] trapAnimations;
    private LinkedList<ProjectileAnimation> projectiles;
    private Map map;

    private final int h = 500, w = 890;

    public PlayScreen( MyGame myGame, Game game ) {

        this.myGame = myGame;
        this.game = game;
        font = new BitmapFont();

        camera = new OrthographicCamera( w, h );

        hero_animations = new HeroAnimation( "Hero\\hero1_fire.atlas", "Hero\\hero1_still.atlas",
                                                    "Hero\\hero1_still.atlas", "Hero\\hero1_still.atlas", 1/10f, 1/3f );
        enemies = new LinkedList<EnemyAnimation>();
        trapAnimations = new TrapAnimation[26];
        projectiles = new LinkedList<ProjectileAnimation>();
        map = new Map();
    }


    public float getRelativeY( int y ) {
        return h * y / Gdx.graphics.getHeight();
    }

    public float getRelativeX( int x ) {
        return w * x / Gdx.graphics.getWidth();
    }

    @Override
    public void show() {

    }

    public void drawLifeBard( float x, float y, TextureRegion[] textures  ) {
        float width = 0;
        for( TextureRegion t : textures ) {
            myGame.batch.draw(t, x + width, y);
            width += t.getRegionWidth();
        }
    }

    @Override
    public void render(float delta) {

        /* UPDATE GAME'S LOGIC */
        /* To Do */
        //Hero's position
        Vector2 hPos = game.getHero().getPosition();
        game.update( delta );

        if( game.getState() == Game.GameStatus.LOST || game.getState() == Game.GameStatus.WON )
            myGame.changeScreen(MyGame.States.MENU);

        String str = "Hero health: " + game.getHero().getStats().getHealth();

        /* UPDATE ALL ANIMATIONS */
        /* In development */

        //Hero's animation
        TextureRegion hero_text = hero_animations.getTexture( game.getHero().getNextState(), delta );
        game.getHero().AnimationStatus( hero_animations.getState() );

        boolean[] frameEvents = game.getFrameEvents();
        if( frameEvents[Game.ENEMY_SPAWN_INDEX] )
            enemies.add( new EnemyAnimation( "Robot\\robot1_attack.atlas", "Robot\\robot1_walk.atlas", 1/5f, 1/3f ));
        if( frameEvents[Game.PROJECTILE_FIRED_INDEX] )
            projectiles.add( new ProjectileAnimation( "Projectile\\projectile1.atlas" ));
        game.setFrameEvents();

        /* DRAW TEXTURES ON THE SCREEN */

        //Clear screen with certain color
        Gdx.gl.glClearColor((float)0.5, (float)0.5, (float)0.5, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Calculate middle of the screen according to the hero's position
        Vector2 midScreen = calMidScreen( hPos, hero_text.getRegionWidth() );

        //Set batch to only draw what the camera sees
        myGame.batch.setProjectionMatrix( camera.combined );

        myGame.batch.begin();

        //Set camera position to match hero's center position
        camera.position.set( midScreen.x, midScreen.y, 0 );
        camera.update();

        //Draw hero's texture
        myGame.batch.draw( map.getSky(), 0, 0 );

        //Iterate throw the traps' animations
        Trap[] traps = game.getTraps();
        for( int i = 0; i < traps.length; i++ ) {
            if( traps[i] == null )
                continue;

            Trap t = traps[i];
            if( trapAnimations[i] == null )
                trapAnimations[i] = new TrapAnimation("Trap\\trap1.atlas", 1/10f, 3);

            myGame.batch.draw(trapAnimations[i].getTexture(t.getState(), delta),t.getPosition().x,t.getPosition().y);
        }

        //Iterate throw the enemies' animations
        LinkedList<Enemy> en = game.getEnemies();
        for( int i = 0; i < enemies.size(); i++ ) {
            Enemy e = en.get(i);
            TextureRegion robot_text = enemies.get(i).getTexture( e.getNextState(), delta );
            myGame.batch.draw(robot_text, e.getPosition().x,e.getPosition().y);
            drawLifeBard( e.getPosition().x + robot_text.getRegionWidth() / 3, e.getPosition().y + robot_text.getRegionHeight(),
                    LifeBar.getTexture( e.getStats().getHealth(), e.getStats().getMaxHealth() ));

            if( e.getState() == Enemy.EnemyStatus.DEAD ) {
                enemies.remove(i);
                game.eraseEnemy(i);
                i--;
            } else e.AnimationStatus( enemies.get(i).getStatus() );
        }

        //Iterate throw the projectiles' animations
        LinkedList<Projectile> proj = game.getProjectiles();
        for( int i = 0; i < projectiles.size(); i++ ) {
            ProjectileAnimation p_ani = projectiles.get(i);
            Projectile p = proj.get(i);
            TextureRegion project_text = p_ani.getTexture( p.getState(), delta );
            myGame.batch.draw(project_text, p.getPosition().x, p.getPosition().y);

            //If the projectile's animation has ended or if the bullet is already out of the map
            if( p_ani.isFinished() || p.getPosition().x <= 0 ) {
                projectiles.remove(i);
                game.eraseProjectile(i);
                i--;
            }
        }

        myGame.batch.draw( hero_text, hPos.x, hPos.y );
        drawLifeBard( hPos.x + hero_text.getRegionWidth() / 3, hPos.y + hero_text.getRegionHeight(),
                LifeBar.getTexture( game.getHero().getStats().getHealth(), game.getHero().getStats().getMaxHealth() ));

        myGame.batch.draw( map.getTerrain(), 0, 0);
        font.draw( myGame.batch, str, hPos.x, hPos.y - 10 );

        myGame.batch.end();
    }

    /**
     * @brief Calculates the center of the screen according to the hero's position
     * @param hPos
     * @param spriteWidth
     * @return
     */
    private Vector2 calMidScreen ( Vector2 hPos, float spriteWidth ) {
        float tmp = hPos.x + spriteWidth / 2;
        return new Vector2( (tmp < 350 ) ? 350 : (tmp > 3650 ) ? 3650 : tmp, 250);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        hero_animations.dispose();
        map.dispose();
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.touchDown( getRelativeX(screenX), getRelativeY(screenY) );
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        game.touchUp( );
        return true;
    }

    public Game getGame(){
        return game;
    }
}
