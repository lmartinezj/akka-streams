package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.japi.function.Function;
import akka.stream.ActorAttributes;
import akka.stream.Supervision;
import akka.stream.javadsl.Flow;

class QualityAssurance {
    private final Flow<UnfinishedCar, Car, NotUsed> inspect;

    QualityAssurance() {
        Function<Throwable, Supervision.Directive> decider = exception -> {
            return (exception instanceof CarFailedInspection ? Supervision.resume(): Supervision.stop());
        };

        this.inspect = Flow.of(UnfinishedCar.class)
                .map(unfinishedCar -> {
                    if (isValid(unfinishedCar)) {
                        return new Car(new SerialNumber(),
                                unfinishedCar.getColor().get(),
                                unfinishedCar.getEngine().get(),
                                unfinishedCar.getWheels(),
                                unfinishedCar.getUpgrade()
                        );
                    }
                    throw new CarFailedInspection(unfinishedCar);
                })
                .withAttributes(ActorAttributes.withSupervisionStrategy(decider));
    }

    Flow<UnfinishedCar, Car, NotUsed> getInspect() {
        return inspect;
    }

    private boolean isValid(UnfinishedCar unfinishedCar) {
        return unfinishedCar.getColor().isPresent() &&
                unfinishedCar.getWheels().size() == 4 &&
                unfinishedCar.getEngine().isPresent();
    }

    static class CarFailedInspection extends IllegalStateException {

        public CarFailedInspection(UnfinishedCar unfinishedCar) {
            super("The car " + unfinishedCar + " failed the inspection ");
        }
    }
}
