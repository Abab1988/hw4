package org.example;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

/**
 * Работник питстопа, меняет шину на прибывшей машинсве на оем месте
 */
@Log4j2
public class PitWorker extends Thread {

    //Место работника, он же номер колеса от 0 до 3
    private final int position;

    //Ссылка на сущность питстопа для связи
    private final PitStop pitStop;


    public PitWorker(int position, PitStop pitStop) {
        this.position = position;
        this.pitStop = pitStop;
    }

    @SneakyThrows
    @Override
    public void run() {
            while (!isInterrupted()) {
                //TODO работник ждет машину на питстопе и меняет шину на своей позиции
                F1Cars car = pitStop.getCar();
                if (car == null) continue;
                car.getWheel(position).replaceWheel();
                log.info("{} заменено колесо {}", car.getCarId(), position);
                pitStop.cyclicBarrier.await();
            }
    }
}
