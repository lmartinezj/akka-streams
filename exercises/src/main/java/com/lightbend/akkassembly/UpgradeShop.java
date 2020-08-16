package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.actor.Cancellable;
import akka.stream.*;
import akka.stream.javadsl.*;
import com.typesafe.config.ConfigException;
import scala.annotation.meta.field;

class UpgradeShop {
    private final Flow<UnfinishedCar, UnfinishedCar, NotUsed> installUpgrades;

    UpgradeShop() {
        this.installUpgrades = Flow.fromGraph(GraphDSL.create(builder -> {
            UniformFanOutShape<UnfinishedCar, UnfinishedCar> uniformFanOutShape = builder
                    .add(Balance.create(3));

            FlowShape<UnfinishedCar,UnfinishedCar> DXFlowShape = builder
                    .add(Flow.of(UnfinishedCar.class)
                            .map(car -> car.installUpgrade(Upgrade.DX)));
            FlowShape<UnfinishedCar,UnfinishedCar> sportFlowShape = builder
                    .add(Flow.of(UnfinishedCar.class)
                            .map(car -> car.installUpgrade(Upgrade.Sport)));
            FlowShape<UnfinishedCar,UnfinishedCar> standardFlowShape = builder
                    .add(Flow.of(UnfinishedCar.class));

            UniformFanInShape<UnfinishedCar, UnfinishedCar> uniformFanInShape = builder
                    .add(Merge.create(3));

            builder.from(uniformFanOutShape.out(0))
                    .via(DXFlowShape)
                    .toInlet(uniformFanInShape.in(0));

            builder.from(uniformFanOutShape.out(1))
                    .via(sportFlowShape)
                    .toInlet(uniformFanInShape.in(1));

            builder.from(uniformFanOutShape.out(2))
                    .via(standardFlowShape)
                    .toInlet(uniformFanInShape.in(2));

            return FlowShape.of(uniformFanOutShape.in(), uniformFanInShape.out());
        }));
    }

    Flow<UnfinishedCar, UnfinishedCar, NotUsed> getInstallUpgrades() {
        return installUpgrades;
    }
}

