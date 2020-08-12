package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import scala.annotation.meta.field;

class WheelShop {
    private final Source<Wheel, NotUsed> wheels;
    private final Flow<UnfinishedCar, UnfinishedCar, NotUsed> installWheels;

    WheelShop() {
        this.wheels = Source.repeat(new Wheel());
        this.installWheels = Flow.of(UnfinishedCar.class)
                .zip(getWheels().grouped(4))
                .map(carAnd4Wheels ->
                        carAnd4Wheels.first()
                        .installWheels(carAnd4Wheels.second())
                );
    }

    Source<Wheel, NotUsed> getWheels() {
        return wheels;
    }

    Flow<UnfinishedCar, UnfinishedCar, NotUsed> getInstallWheels() {
        return installWheels;
    }
}
