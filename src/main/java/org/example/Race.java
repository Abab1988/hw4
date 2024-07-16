package org.example;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

@Log4j2
public class Race {

    @Getter
    private long distance;

    private List<F1Cars> participantCars = new java.util.ArrayList<>();

    private List<Team> teams = new java.util.ArrayList<>();

    private LocalDateTime raceStartTime;


    private final CyclicBarrier cyclicBarrie;

    @Getter
    private final CountDownLatch finishLatch;

    public Race(long distance, Team[] participantCars) {
        this.distance = distance;
        teams.addAll(List.of(participantCars));
        this.finishLatch = new CountDownLatch(teams.size() * 2);
        this.cyclicBarrie = new CyclicBarrier(teams.size() * 2, () -> this.raceStartTime = LocalDateTime.now());
    }

    /**
     * Запускаем гонку
     */
    public void start() {
        for (Team team : teams) {
            team.prepareRace(this);
        }
        //TODO даем команду на старт гонки
        try {
            //TODO блокируем поток до завершения гонки
            finishLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //Регистрируем участников гонки
    public void register(F1Cars participantCar) {
        participantCars.add(participantCar);
    }


    public void start(F1Cars f1Cars) {
        try {
            log.info("Боллид {} занял свое место на старте.", f1Cars.getCarId());
            cyclicBarrie.await();
            log.info("Старт! Боллид {} тронулся с места.", f1Cars.getCarId());
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }

    }

    public long finish(F1Cars participant) {
        log.info("Финиш! Боллид {} финишировал", participant.getCarId());
        long time = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - raceStartTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        participant.setTime(time);
        finishLatch.countDown();
        return time;
    }

    public void printResults() {
        participantCars.sort(F1Cars::compareTo);
        log.info("Результат гонки:");
        int position = 0;
        for (F1Cars participant : participantCars) {
            log.info("Позиция: {} номер боллида: {} время: {} мс", position++, participant.getName(), participant.getTime());
        }
    }

}
