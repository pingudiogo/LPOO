package com.lpoo.project.logic;

import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

/**
 * Class that provides the creation of the game
 * This class implements the interface Updatable
 */
public class Game implements Updatable {

    /**
     * Index of the melee's spawn
     */
    public static final int ENEMY_MELEE_SPAWN_INDEX = 0;
    /**
     * Index of the ranged's spawn
     */
    public static final int ENEMY_RANGED_SPAWN_INDEX = 1;
    /**
     * Index of the hero when the projectile is fired
     */
    public static final int HERO_PROJECTILE_FIRED_INDEX = 2;
    /**
     * Index of the enemy when the projectile is fired
     */
    public static final int ENEMY_PROJECTILE_FIRED_INDEX = 3;

    /**
     * Array with the frame's events
     */
    private boolean[] frameEvents;
    /**
     * Number of projectiles
     */
    public int nNewProjectiles;

    /**
     * Enumeration about the game's status
     */
    public enum GameStatus {
        BUILDING, PLAYING, LOST
    }

    /**
     * Game's status
     */
    private GameStatus state;

    /**
     * Game's hero
     */
    private Hero hero;

    /**
     * Vector with the enemies stats (melee and ranged)
     */
    private Vector<CharacterStats> enemiesStats;

    /**
     * LinkedList with all the enemies of the game
     */
    private LinkedList<Enemy> enemies;
    /**
     * Array with the traps
     */
    private Trap[] traps;
    /**
     * LinkedList with all the projectiles
     */
    private LinkedList<Projectile> projectiles;

    /**
     * Status' "time of life"
     */
    public float stateTime;

    /**
     * Wave's number
     */
    private int wave = 0;
    /**
     * Number of enemies
     */
    private int nEnemies = 10;
    /**
     * Number of enemies which won
     */
    private int nEnemiesWon = 0;
    /**
     * Maximum number of enemies that is needed to the enemies won the game
     */
    private static final int maxEnemiesWon = 3;
    /**
     * Number of enemies spawned
     */
    private int enemiesSpawned = 0;
    /**
     * Time difference between enemies
     */
    private int diffNextEnemy = 5;

    /**
     * Enemies' resistance per wave
     */
    private final int resistPerWave = 1;
    /**
     * Enemies' health per wave
     */
    private final int healthPerWave = 10;
    /**
     * Enemies' strength per wave
     */
    private final int strengthPerWave = 1;

    /**
     * Hero's initial money
     */
    private int money = 0;
    /**
     * Traps' initial cost
     */
    private int trapCost = 100;
    /**
     * Initial score
     */
    private int score = 0;

    /**
     * Constructor for the class Game
     *
     * @param heroStats Hero's properties
     * @param enemiesStats Enemies' properties
     */
    public Game(CharacterStats heroStats, Vector<CharacterStats> enemiesStats) {
        frameEvents = new boolean[4];
        frameEvents[ENEMY_MELEE_SPAWN_INDEX] = false;
        frameEvents[ENEMY_RANGED_SPAWN_INDEX] = false;
        frameEvents[HERO_PROJECTILE_FIRED_INDEX] = false;
        frameEvents[ENEMY_PROJECTILE_FIRED_INDEX] = false;
        nNewProjectiles = 0;

        int x = 300;
        int y = 144;

        hero = new Hero(this, x, y, heroStats);

        enemies = new LinkedList<>();
        traps = new Trap[26];
        projectiles = new LinkedList<>();

        this.enemiesStats = enemiesStats;

        state = GameStatus.BUILDING;
        stateTime = 0;
    }

    /**
     * Getter for the game's status
     *
     * @return The game's status
     */
    public GameStatus getState() {
        return state;
    }

    /**
     * Getter for the wave variable
     *
     * @return the current wave being played
     */
    public int getWave() {
        return wave;
    }

    /**
     * Updates the game  playing
     *
     * @param delta Difference between the last time of call and the current time
     */
    public void updatePlaying(float delta) {

        if (nEnemiesWon >= maxEnemiesWon) {
            state = GameStatus.LOST;
            return;
        }

        float currTime = stateTime + delta;
        hero.update(delta);

        for (Enemy e : enemies) {
            if (e.getPosition().x >= 4000) {
                nEnemiesWon++;
                e.setStates(Enemy.EnemyStatus.DEAD);
            } else e.update(delta);
        }
        for (Projectile p : projectiles)
            p.update(delta);
        for (Trap t : traps) {
            if (t == null)
                continue;
            t.update(delta);
        }

        int nextEnemy = wave >= diffNextEnemy ? 1 : diffNextEnemy / wave;
        if (enemiesSpawned < nEnemies + 4 * wave &&
                Math.floor(stateTime / (float) nextEnemy) != Math.floor(currTime / (float) nextEnemy)) {
            enemiesSpawned++;
            Enemy e;
            Random rand = new Random();
            int type = rand.nextInt(2);

            CharacterStats temp = enemiesStats.elementAt(type);
            CharacterStats stats = new CharacterStats(
                    temp.getHealth() + healthPerWave * wave,
                    temp.getResistance() + resistPerWave * wave,
                    temp.getMovSpeed(),
                    temp.getAttSpeed(),
                    temp.getAttDamage() + strengthPerWave * wave);

            if (type == 0) {
                e = new MeleeEnemy(this, 50, 144, stats);
                frameEvents[ENEMY_MELEE_SPAWN_INDEX] = true;
            } else {
                e = new RangedEnemy(this, 50, 144, stats);
                frameEvents[ENEMY_RANGED_SPAWN_INDEX] = true;
            }
            addEnemy(e);
        }

        stateTime = currTime;
    }

    /**
     * Updates the game and the current status
     *
     * @param delta Difference between the last time of call and the current time
     */
    public void update(float delta) {
        if (state == GameStatus.PLAYING)
            if (enemiesSpawned == nEnemies + 4 * wave && enemies.size() == 0) {
                stateTime = 0;
                state = GameStatus.BUILDING;
            } else updatePlaying(delta);
    }

    /**
     * Changes the current status of the game
     *
     * @param status New status that will replace the old one
     */
    public void changeState(GameStatus status) {
        if (status == GameStatus.PLAYING) {
            state = status;
            enemies.clear();
            projectiles.clear();
            enemiesSpawned = 0;
            wave++;
        }
    }

    /**
     * Getter for the frame's events
     *
     * @return An array with the boolean values of frame's events
     */
    public boolean[] getFrameEvents() {
        return frameEvents;
    }

    /**
     * Sets all the frameEvents to false
     */
    public void setFrameEvents() {
        for (int i = 0; i < frameEvents.length; i++)
            frameEvents[i] = false;
        nNewProjectiles = 0;
    }

    /**
     * Getter for the hero
     *
     * @return The game's hero
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Setter for the hero's amount of money
     *
     * @param money New hero's amount of money
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Getter for the hero's amount of money
     *
     * @return The hero's current amount of money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Getter for the hero's current score
     *
     * @return The hero's current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for the number of enemies that won
     *
     * @return The number of enemies that won
     */
    public int getnEnemiesWon() {
        return nEnemiesWon;
    }

    /**
     * Setter for the number of enemies the have survived
     *
     * @param nEnemiesWon value to replace nEnemiesWon
     */
    public void setnEnemiesWon(int nEnemiesWon) {
        this.nEnemiesWon = nEnemiesWon;
    }

    /**
     * Getter for the maximum number of enemies that is needed to the enemies won the game
     *
     * @return The maximum number of enemies that is needed to the enemies won the game
     */
    public static int getMaxEnemiesWon() {
        return maxEnemiesWon;
    }

    /**
     * Getter for the game's enemies
     *
     * @return The game's enemies
     */
    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Getter for the projectiles
     *
     * @return The projectiles
     */
    public LinkedList<Projectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Getter for the number of projectiles
     *
     * @return The number of projectiles
     */
    public int getnNewProjectiles() {
        return nNewProjectiles;
    }

    /**
     * Getter for the game's traps
     *
     * @return The game's traps
     */
    public final Trap[] getTraps() {
        return traps;
    }

    /**
     * Getter for the number of enemies that spawned in a certain wave
     *
     * @return the number of enemies spawned
     */
    public int getEnemiesSpawned() {
        return enemiesSpawned;
    }

    /**
     * Setter for the number of enemies spawned
     *
     * @param enemiesSpawned value to be replaced as the number of enemies spawned
     */
    public void setEnemiesSpawned(int enemiesSpawned) {
        this.enemiesSpawned = enemiesSpawned;
    }

    /**
     * Adds a enemy to the LinkedList of enemies
     *
     * @param enemy New enemy to be added
     */
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * Adds a projectile to the LinkedList of projectiles
     *
     * @param projectile New projectile to be added
     * @param heroSide   Reprsents the hero sido to shoot the gun
     */
    public void addProjectile(Projectile projectile, boolean heroSide) {
        if (heroSide)
            frameEvents[HERO_PROJECTILE_FIRED_INDEX] = true;
        else {
            nNewProjectiles++;
            frameEvents[ENEMY_PROJECTILE_FIRED_INDEX] = true;
        }
        projectiles.add(projectile);
    }

    /**
     * Erases an enemy from a certain index
     *
     * @param index Index where the enemy will be erased
     */
    public void eraseEnemy(int index) {
        money += 20;
        score += enemies.get(index).getPoints();
        enemies.remove(index);
    }

    /**
     * Erses an projectile from a certain index
     *
     * @param index Index where the projectile will be erased
     */
    public void eraseProjectile(int index) {
        projectiles.remove(index);
    }

    public void heroMove(int dir) {
        hero.move(dir);
    }

    public void heroAttack() {
        hero.attack();
    }

    public void stopHero() {
        hero.stop();
    }

    /**
     * Setter for the trap
     * This functions allows the user to put a trap wherever it wants
     *
     * @param x      Trap's x position
     * @param y      Trap's y position
     * @param width  Trap's width
     * @param height Trap's height
     * @param index  Trap's index
     */
    public void setTrap(int x, int y, int width, int height, int index) {
        if (index < 0 || index >= traps.length)
            return;

        if (traps[index] == null && money >= trapCost) {
            money -= trapCost;
            trapCost += 10;
            traps[index] = new Trap(this, x, y, width, height, 5);
        } else if (traps[index] != null) {
            trapCost -= 10;
            money += trapCost;
            traps[index] = null;
        }
    }

    /**
     * Changes the game status to LOST
     */
    public void lose() {
        state = GameStatus.LOST;
    }

}
