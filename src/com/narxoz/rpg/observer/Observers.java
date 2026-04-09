package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;
import java.util.*;

public class Observers {
    public static class BattleLogger implements GameObserver {
        public void onEvent(GameEvent e) {
            System.out.println("[LOG] " + e.getType() + " | SOURCE: " + e.getSourceName() + " | VALUE: " + e.getValue());
        }
    }

    public static class AchievementTracker implements GameObserver {
        private final Set<String> unlocked = new HashSet<>();
        public void onEvent(GameEvent e) {
            if (e.getType() == GameEventType.ATTACK_LANDED && unlocked.add("FIRST BLOOD")) System.out.println("[ACHIEVEMENT] FIRST BLOOD UNLOCKED!");
            if (e.getType() == GameEventType.BOSS_DEFEATED && unlocked.add("BOSS SLAYER")) System.out.println("[ACHIEVEMENT] BOSS SLAYER UNLOCKED!");
            if (e.getType() == GameEventType.HERO_DIED && unlocked.add("FALLEN COMRADE")) System.out.println("[ACHIEVEMENT] FALLEN COMRADE UNLOCKED!");
        }
    }

    public static class PartySupport implements GameObserver {
        private final List<Hero> party;
        public PartySupport(List<Hero> party) { this.party = party; }
        public void onEvent(GameEvent e) {
            if (e.getType() == GameEventType.HERO_LOW_HP) {
                for (Hero h : party) if (h.isAlive()) { h.heal(25); break; }
                System.out.println("[SUPPORT] PARTY HEALED!");
            }
        }
    }

    public static class HeroStatusMonitor implements GameObserver {
        private final List<Hero> party;
        public HeroStatusMonitor(List<Hero> party) { this.party = party; }
        public void onEvent(GameEvent e) {
            if (e.getType() == GameEventType.HERO_LOW_HP || e.getType() == GameEventType.HERO_DIED) {
                System.out.println("--- PARTY STATUS ---");
                for (Hero h : party) System.out.println(h.getName() + ": " + h.getHp() + " HP " + (h.isAlive() ? "" : "(DEAD)"));
            }
        }
    }

    public static class LootDropper implements GameObserver {
        public void onEvent(GameEvent e) {
            if (e.getType() == GameEventType.BOSS_PHASE_CHANGED) System.out.println("[LOOT] BOSS DROPPED RARE SHARD!");
            if (e.getType() == GameEventType.BOSS_DEFEATED) System.out.println("[LOOT] BOSS DROPPED LEGENDARY SWORD!");
        }
    }
}

