package com.narxoz.rpg.engine;

import com.narxoz.rpg.combatant.*;
import com.narxoz.rpg.observer.*;
import com.narxoz.rpg.strategy.BalancedStrategy;
import java.util.List;

public class DungeonEngine {
    private final List<Hero> heroes;
    private final DungeonBoss boss;
    private final EventPublisher publisher;

    public DungeonEngine(List<Hero> heroes, DungeonBoss boss, EventPublisher publisher) {
        this.heroes = heroes;
        this.boss = boss;
        this.publisher = publisher;
    }

    public EncounterResult run() {
        int rounds = 0;

        while (rounds < 100 && boss.isAlive() && heroes.stream().anyMatch(Hero::isAlive)) {
            rounds++;

            
            if (rounds == 3 && heroes.get(0).isAlive()) {
                heroes.get(0).setStrategy(new BalancedStrategy());
                System.out.println(">>> " + heroes.get(0).getName() + " SWITCHED STRATEGY TO BALANCED");
            }

            for (Hero h : heroes) {
                if (h.isAlive() && boss.isAlive()) {
                    int dmg = Math.max(1, h.getStrategy().calculateDamage(h.getAttackPower()) - boss.getStrategy().calculateDefense(boss.getDefense()));
                    boss.takeDamage(dmg);
                    publisher.broadcastEvent(new GameEvent(GameEventType.ATTACK_LANDED, h.getName(), dmg));
                }
            }

            if (!boss.isAlive()) {
                publisher.broadcastEvent(new GameEvent(GameEventType.BOSS_DEFEATED, boss.getName(), rounds));
                break;
            }

            for (Hero h : heroes) {
                if (h.isAlive()) {
                    int dmg = Math.max(1, boss.getStrategy().calculateDamage(boss.getAttackPower()) - h.getStrategy().calculateDefense(h.getDefense()));
                    h.takeDamage(dmg);
                    publisher.broadcastEvent(new GameEvent(GameEventType.ATTACK_LANDED, boss.getName(), dmg));

                    if (h.getHp() <= h.getMaxHp() * 0.3 && h.isAlive() && !h.isLowHpTriggered()) {
                        h.setLowHpTriggered(true);
                        publisher.broadcastEvent(new GameEvent(GameEventType.HERO_LOW_HP, h.getName(), h.getHp()));
                    }

                    if (!h.isAlive()) {
                        publisher.broadcastEvent(new GameEvent(GameEventType.HERO_DIED, h.getName(), 0));
                    }
                }
            }
        }

        return new EncounterResult(boss.getHp() <= 0, rounds, (int) heroes.stream().filter(Hero::isAlive).count());
    }
}


