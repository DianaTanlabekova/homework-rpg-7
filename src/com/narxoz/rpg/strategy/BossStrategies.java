package com.narxoz.rpg.strategy;

public class BossStrategies {
    public static class InitialStage implements CombatStrategy {
        public int calculateDamage(int p) { return (int)(p * 1.1); }
        public int calculateDefense(int d) { return (int)(d * 1.2); }
        public String getName() { return "STAGE 1: CALM AND STEADY"; }
    }

    public static class FuryStage implements CombatStrategy {
        public int calculateDamage(int p) { return (int)(p * 1.6); }
        public int calculateDefense(int d) { return (int)(d * 0.6); }
        public String getName() { return "STAGE 2: UNLEASHED ANGER"; }
    }

    public static class FinalStandStage implements CombatStrategy {
        public int calculateDamage(int p) { return (int)(p * 2.5); }
        public int calculateDefense(int d) { return 0; }
        public String getName() { return "STAGE 3: TOTAL CHAOS"; }
    }
}