package com.lightbend.akkassembly;

import akka.stream.javadsl.Sink;
import org.junit.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

public class BodyShopTest extends AkkaSpec {

    @Test
    public void cars_shouldReturnCarsAtTheExpectedRate() {
        BodyShop bodyShop = new BodyShop(Duration.ofMillis(200));

        List<UnfinishedCar> cars = bodyShop.getCars()
                .takeWithin(Duration.ofMillis(1100))
                .runWith(Sink.seq(), system)
                .toCompletableFuture()
                .join();

        assertEquals(5, cars.size());
    }

}
