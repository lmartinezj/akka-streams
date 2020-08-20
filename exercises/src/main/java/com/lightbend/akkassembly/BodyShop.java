package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.actor.Cancellable;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;

import java.time.Duration;

class BodyShop {

    private final Source<UnfinishedCar, NotUsed> cars;

    BodyShop(Duration buildTime) {
        this.cars = Source.repeat(new UnfinishedCar()).throttle(1, buildTime);
    }

    Source<UnfinishedCar, NotUsed> getCars() {
        return cars;
    }
}
