package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.stream.javadsl.Flow;

class QualityAssurance {
    private final Flow<UnfinishedCar, Car, NotUsed> inspect;

    QualityAssurance() {
        this.inspect = Flow.of(UnfinishedCar.class).
                filter(this::isValid)
                .map(unfinishedCar ->
                        new Car(new SerialNumber(),
                                unfinishedCar.getColor().get(),
                                unfinishedCar.getEngine().get(),
                                unfinishedCar.getWheels(),
                                unfinishedCar.getUpgrade()
                        )
                );
    }

    Flow<UnfinishedCar, Car, NotUsed> getInspect() {
        return inspect;
    }

    private boolean isValid(UnfinishedCar unfinishedCar) {
        return unfinishedCar.getColor().isPresent() &&
                unfinishedCar.getWheels().size() == 4 &&
                unfinishedCar.getEngine().isPresent();
    }
}
