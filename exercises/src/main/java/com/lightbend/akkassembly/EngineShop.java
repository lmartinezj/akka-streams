package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.stream.javadsl.Source;

import java.util.Vector;
import java.util.stream.Stream;


class EngineShop {
    private final Source<Shipment, NotUsed> shipments;

    EngineShop(int shipmentSize) {
        this.shipments = Source.fromIterator(() -> Stream.generate(() -> {
            Vector<Engine> engines = new Vector<>(shipmentSize);
            for (int i = 0; i < shipmentSize; i++) {
                engines.add(new Engine());
            }
            return new Shipment(engines);
        }).iterator());
    }

    Source<Shipment, NotUsed> getShipments() {
        return shipments;
    }
}

