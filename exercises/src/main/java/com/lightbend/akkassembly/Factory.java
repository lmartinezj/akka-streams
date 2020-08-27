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
                .via(paintShop.getPaint().named("paint-stage"))
                .via(engineShop.getInstallEngine().named("install-engine-stage"))
                .async()
                .via(wheelShop.getInstallWheels().named("install-wheels-stage"))
                .async()
                .via(upgradeShop.getInstallUpgrades().named("install-upgrades-stage"))
                .via(qualityAssurance.getInspect().named("inspect-stage"))
                .take(quantity)
                .runWith(Sink.seq(), system);
    }
}
