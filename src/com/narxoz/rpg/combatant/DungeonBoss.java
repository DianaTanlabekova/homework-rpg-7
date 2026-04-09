package com.narxoz.rpg.combatant;

import com.narxoz.rpg.observer.*;
import com.narxoz.rpg.strategy.BossStrategies;
import com.narxoz.rpg.strategy.CombatStrategy;


public class DungeonBoss implements GameObserver {
    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private CombatStrategy strategy;
    private int currentPhase = 1;
    private final EventPublisher publisher;


    public DungeonBoss(String name, int maxHp, int attackPower, int defense, EventPublisher publisher) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.publisher = publisher;
        this.strategy = new BossStrategies.InitialStage();
    }


    public void takeDamage(int amount) {
        int oldHp = this.hp;
        this.hp = Math.max(0, this.hp - amount);
        checkPhaseChange(oldHp);
    }


    private void checkPhaseChange(int oldHp) {
        double oldPct = (double) oldHp / maxHp;
        double newPct = (double) this.hp / maxHp;


        if (oldPct >= 0.6 && newPct < 0.6) {
            publisher.broadcastEvent(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 2));
        }
        if (oldPct >= 0.3 && newPct < 0.3) {
            publisher.broadcastEvent(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 3));
        }
    }


    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED) {
            int newPhase = event.getValue();
            if (newPhase > this.currentPhase) {
                this.currentPhase = newPhase;
                if (currentPhase == 2) {
                    this.strategy = new BossStrategies.FuryStage();
                } else if (currentPhase == 3) {
                    this.strategy = new BossStrategies.FinalStandStage();
                }
                System.out.println(">>> BOSS ENTERED PHASE " + currentPhase + " USING " + strategy.getName());
            }
        }
    }


    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttackPower() { return attackPower; }
    public int getDefense() { return defense; }
    public CombatStrategy getStrategy() { return strategy; }
    public boolean isAlive() { return hp > 0; }
}





