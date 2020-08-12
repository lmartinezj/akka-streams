package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import scala.annotation.meta.field;

import java.util.Vector;
import java.util.stream.Stream;


class EngineShop {
    private final Source<Shipment, NotUsed> shipments;
    private final Source<Engine, NotUsed> engines;
    private final Flow<UnfinishedCar, UnfinishedCar, NotUsed> installEngine;

    EngineShop(int shipmentSize) {
        this.shipments = Source.fromIterator(() -> Stream.generate(() -> {
            Vector<Engine> engines = new Vector<>(shipmentSize);
            for (int i = 0; i < shipmentSize; i++) {
                engines.add(new Engine());
            }
            return new Shipment(engines);
        }).iterator());

        this.engines = shipments.mapConcat(Shipment::getEngines);

        this.installEngine = Flow.of(UnfinishedCar.class)
                .zip(engines)
                .map(carEnginePair ->
                        carEnginePair.first()
                                .installEngine(
                                        carEnginePair.second()
                                )
                );
    }

    Source<Shipment, NotUsed> getShipments() {
        return shipments;
    }

    Source<Engine, NotUsed> getEngines() {
        return engines;
    }

    Flow<UnfinishedCar, UnfinishedCar, NotUsed> getInstallEngine() {
        return installEngine;
    }
}

