package com.lightbend.akkassembly;

import akka.actor.ActorSystem;
import akka.stream.javadsl.Sink;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class Factory {
    private final BodyShop bodyShop;
    private final PaintShop paintShop;
    private final EngineShop engineShop;
    private final WheelShop wheelShop;
    private final UpgradeShop upgradeShop;
    private final QualityAssurance qualityAssurance;
    private final ActorSystem system;

    Factory(BodyShop bodyShop, PaintShop paintShop,
            EngineShop engineShop, WheelShop wheelShop,
            QualityAssurance qualityAssurance, UpgradeShop upgradeShop,
            ActorSystem system) {
        this.bodyShop = bodyShop;
        this.paintShop = paintShop;
        this.engineShop = engineShop;
        this.wheelShop = wheelShop;
        this.upgradeShop = upgradeShop;
        this.qualityAssurance = qualityAssurance;
        this.system = system;
    }

    CompletionStage<List<Car>> orderCars(int quantity) {
        return bodyShop.getCars()
                .via(paintShop.getPaint())
                .async()
                .via(engineShop.getInstallEngine())
                .async()
                .via(wheelShop.getInstallWheels())
                .async()
                .via(upgradeShop.getInstallUpgrades())
                .via(qualityAssurance.getInspect())
                .take(quantity)
                .runWith(Sink.seq(), system);
    }
}
