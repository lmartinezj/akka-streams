package com.lightbend.akkassembly;

import akka.stream.javadsl.Sink;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class EngineShopTest extends AkkaSpec {

    @Test
    public void shipments_shouldEmitASeriesOfUniqueShipments() {
        int shipmentSize = 10;
        int numberToRequest = 5;

        EngineShop engineShop = new EngineShop(shipmentSize);

        List<Shipment> shipments = engineShop.getShipments()
                .take(numberToRequest)
                .runWith(Sink.seq(), system)
                .toCompletableFuture()
                .join();

        for(Shipment shipment : shipments) {
            assertEquals(shipmentSize, new HashSet<>(shipment.getEngines()).size());
        }
    }

    @Test
    public void shipments_shouldEmitUniqueEnginesFromOneShipmentToTheNext() {
        int shipmentSize = 1;
        int numberToRequest = 5;

        EngineShop engineShop = new EngineShop(shipmentSize);

        List<Engine> engines = engineShop.getShipments()
                .mapConcat(Shipment::getEngines)
                .take(numberToRequest)
                .runWith(Sink.seq(), system)
                .toCompletableFuture()
                .join();

        assertEquals(numberToRequest, new HashSet<>(engines).size());
    }
}
