package com.lpoo.project.logic;

/**
 * Class that creates the traps' properties
 */
public class TrapStats extends Stats {

    /**
     * Trap's attack damage
     */
    private int attDamage;

    /**
     * Trap's speed of attack
     */
    private float attackSpeed;

    /**
     * Trap's speed of recharge
     */
    private float rechargeSpeed;

    /**
     * Trap's speed of heat up
     */
    private float heatUpSpeed;

    /**
     * Trap's time of attack
     */
    private float timeAttack;

    /**
     * Trap's cost
     */
    private int cost;


    /**
     * Constructor for the class TrapStats
     * @param attDamage Trap's attack damage
     * @param attackSpeed Trap's speed of attack
     * @param rechargeSpeed Trap's speed of recharge
     * @param heatUpSpeed Trap's speed of heat up
     * @param timeAttack Trap's time of attack
     * @param cost Trap's cost
     */
    public TrapStats(int attDamage, float attackSpeed, float rechargeSpeed, float heatUpSpeed, float timeAttack, int cost) {
        super();
        this.attDamage = attDamage;
        this.attackSpeed = attackSpeed;
        this.rechargeSpeed = rechargeSpeed;
        this.heatUpSpeed = heatUpSpeed;
        this.timeAttack = timeAttack;
        this.cost = cost;
    }

    /**
     * Getter for the trap's attack speed
     * @return the trap's attack speed
     */
    public float getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Getter for the trap's heat up speed
     * @return the trap's heat up speed
     */
    public float getHeatUpSpeed() {
        return heatUpSpeed;
    }

    /**
     * Getter for the trap's recharge speed
     * @return the trap's recharge speed
     */
    public float getRechargeSpeed() {
        return rechargeSpeed;
    }

    /**
     * Getter for the trap's time of attack
     * @return the trap's time of attack
     */
    public float getTimeAttack() {
        return timeAttack;
    }

    /**
     * Getter for the trap's cost
     * @return the trap's cost
     */
    public int getCost() {
        return cost;
    }

    @Override
    /**
     * Getter for the attack's damage
     * @return the attack damage
     */
    public int getAttDamage() {
        return attDamage;
    }

    @Override
    /**
     * Creates a string with the character's properties
     */
    public String toString() {
        return "";
    }
}
