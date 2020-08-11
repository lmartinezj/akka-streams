package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.stream.javadsl.Source;

class WheelShop {
    private final Source<Wheel, NotUsed> wheels;

    WheelShop() {
        this.wheels = Source.repeat(new Wheel());
    }

    Source<Wheel, NotUsed> getWheels() {
        return wheels;
    }
}
