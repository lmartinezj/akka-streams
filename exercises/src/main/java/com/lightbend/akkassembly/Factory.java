package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.ActorSystem$;
import akka.actor.Cancellable;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class Factory {
    private final BodyShop bodyShop;
    private final PaintShop paintShop;
    private final EngineShop engineShop;
    private final WheelShop wheelShop;
    private final QualityAssurance qualityAssurance;
    //private final Materializer materializer;
    private final ActorSystem system;

    Factory(BodyShop bodyShop, PaintShop paintShop,

            EngineShop engineShop, WheelShop wheelShop,
            QualityAssurance qualityAssurance, Materializer materializer) {
        this(bodyShop, paintShop, engineShop, wheelShop, qualityAssurance, materializer.system());
    }



    Factory(BodyShop bodyShop, PaintShop paintShop,
            EngineShop engineShop, WheelShop wheelShop,
            QualityAssurance qualityAssurance, ActorSystem system) {
        this.bodyShop = bodyShop;
        this.paintShop = paintShop;
        this.engineShop = engineShop;
        this.wheelShop = wheelShop;
        this.qualityAssurance = qualityAssurance;
        this.system = system;
    }

    CompletionStage<List<Car>> orderCars(int quantity) {
        return bodyShop.getCars()
                .via(paintShop.getPaint())
                .via(engineShop.getInstallEngine())
                .via(wheelShop.getInstallWheels())
                .via(qualityAssurance.getInspect())
                .take(quantity)
                .runWith(Sink.seq(), system);
    }
}
