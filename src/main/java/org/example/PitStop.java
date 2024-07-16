package org.example;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

@Log4j2
public class PitStop extends Thread {

    PitWorker[] workers = new PitWorker[4];

    volatile F1Cars currentCar;

    Semaphore semaphore = new Semaphore(1, true);

    CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

    public PitStop() {
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new PitWorker(i, this);
            workers[i].start();
        }
    }

    public void pitline(F1Cars f1Cars) {
        try {
            semaphore.acquire();
            log.info("Боллид {} заехал на питстоп", f1Cars.getCarId());
            this.currentCar = f1Cars;
            cyclicBarrier.await();
            currentCar = null;
            log.info("Боллид {} - колеса заменены", f1Cars.getCarId());
            semaphore.release();
            log.info("Боллид {} выехал из питстопа", f1Cars.getCarId());
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }


    @Override
    public void run() {
        while(!isInterrupted()) {
        }
    }

    public F1Cars getCar() throws InterruptedException {
        return this.currentCar;
    }
}
