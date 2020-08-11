package com.lightbend.akkassembly;

import akka.stream.javadsl.Sink;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class WheelShopTest extends AkkaSpec {

    @Test
    public void wheels_shouldReturnASeriesOfWheels() {
        int numberToRequest = 100;
        WheelShop wheelShop = new WheelShop();

        List<Wheel> wheels = wheelShop.getWheels()
                .take(numberToRequest)
                .runWith(Sink.seq(), system)
                .toCompletableFuture()
                .join();

        assertEquals(numberToRequest, wheels.size());
    }
}
