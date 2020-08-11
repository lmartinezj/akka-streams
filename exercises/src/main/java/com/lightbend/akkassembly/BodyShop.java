package com.lightbend.akkassembly;

import akka.actor.Cancellable;
import akka.stream.javadsl.Source;

import java.time.Duration;

class BodyShop {

    private final Source<UnfinishedCar, Cancellable> cars;

    BodyShop(Duration buildTime) {
        this.cars = Source.tick(buildTime, buildTime, new UnfinishedCar());
    }

    Source<UnfinishedCar, Cancellable> getCars() {
        return cars;
    }
}
