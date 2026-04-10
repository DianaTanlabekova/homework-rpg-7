package com.narxoz.rpg;

import com.narxoz.rpg.combatant.*;
import com.narxoz.rpg.engine.*;
import com.narxoz.rpg.observer.*;
import com.narxoz.rpg.strategy.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        EventPublisher pub = new EventPublisher();
        
        List<Hero> heroes = new ArrayList<>();
        
        heroes.add(new Hero("WARRIOR RAIDEN", 120, 30, 12)); 
        heroes.add(new Hero("MAGE KAZUHA", 100, 55, 5));     
        heroes.add(new Hero("PALADIN DONATELLO", 150, 28, 20)); 
        
        heroes.get(0).setStrategy(new AggressiveStrategy());
        heroes.get(1).setStrategy(new BalancedStrategy());
        heroes.get(2).setStrategy(new DefensiveStrategy());

    
        DungeonBoss boss = new DungeonBoss("PROTOTYPE", 400, 30, 10, pub);
        
        pub.registerObserver(boss);
        pub.registerObserver(new Observers.BattleLogger());
        pub.registerObserver(new Observers.AchievementTracker());
        pub.registerObserver(new Observers.PartySupport(heroes));
        pub.registerObserver(new Observers.HeroStatusMonitor(heroes));
        pub.registerObserver(new Observers.LootDropper());

        DungeonEngine engine = new DungeonEngine(heroes, boss, pub);
        EncounterResult result = engine.run();

        System.out.println("--- ENCOUNTER RESULT ---");
        System.out.println("WIN: " + result.isHeroesWon());
        System.out.println("ROUNDS: " + result.getRoundsPlayed());
        System.out.println("SURVIVORS: " + result.getSurvivingHeroes());
    }
}