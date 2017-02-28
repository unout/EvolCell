package sample;

import java.util.Random;

public class EvolAlgo1 {

    private static final String target = "METHINKS IT IS LIKE A WEASEL";
    private static final char[] possibilities = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ".toCharArray();

    private static int C = 100;   // number of spawn per generation

    private static double minMutateRate = 0.09;
    private static int perfectFitness = target.length();
    private static String parent;
    private static Random rand = new Random();

    private static int fitness(String trial) {
        int sumOfMatching = 0;
        for (int i = 0; i < trial.length(); i++) {
            // Sum of matching chars by position
            if (trial.charAt(i) == target.charAt(i))
                sumOfMatching++;
        }
        return sumOfMatching;
    }

    private static double newMutateRate() {
        // Less mutation the closer the fit of the parent
        return (((double) perfectFitness - fitness(parent)) / perfectFitness * (1 - minMutateRate));
    }

    private static String mutate(String parent, double rate) {
        String retVal = "";
        for (int i = 0; i < parent.length(); i++) {
            retVal += (rand.nextDouble() <= rate) ?
                    possibilities[rand.nextInt(possibilities.length)] :
                    parent.charAt(i);
        }
        return retVal;
    }

    public static void main(String[] args) {
        parent = mutate(target, 1);
        int iteration = 0;
        while (!target.equals(parent)) {
            double rate = newMutateRate();
            iteration++;
            if (iteration % 100 == 0) {
                System.out.println(iteration + ": " + parent + ", fitness: " + fitness(parent) + ", rate: " + rate);
            }
            String bestSpawn = null;
            int bestFit = 0;
            for (int i = 0; i < C; i++) {
                String spawn = mutate(parent, rate);
                int fitness = fitness(spawn);
                if (fitness > bestFit) {
                    bestSpawn = spawn;
                    bestFit = fitness;
                }
            }
            parent = bestFit > fitness(parent) ? bestSpawn : parent;
        }
        System.out.println(parent + ", " + iteration);
    }

}