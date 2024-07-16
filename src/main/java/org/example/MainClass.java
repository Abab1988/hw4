package org.example;

public class MainClass {
    public static void main(String[] args) {
        Team teams[] = new Team[3];

        for (int i = 0; i < teams.length; i++) {
            teams[i] = new Team(i + 1);
        }

        Race race = new Race(1000, teams);

        race.start();
        race.printResults();
    }
}
