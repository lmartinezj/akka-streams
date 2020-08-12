package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;

import java.util.Set;
import java.util.stream.IntStream;

public class PaintShop {
    private final Source<Color, NotUsed> colors;
    private final Flow<UnfinishedCar, UnfinishedCar, NotUsed> paint;

    PaintShop(Set<Color> colorSet) {
        this.colors = Source.cycle(colorSet::iterator);
        this.paint = Flow.of(UnfinishedCar.class).
                zip(getColors()).map(
                (unfinishedCarColorPair) ->
                        unfinishedCarColorPair
                                .first()
                                .paint(unfinishedCarColorPair.second())
        );
    }

    Source<Color, NotUsed> getColors() {
        return colors;
    }

    Flow<UnfinishedCar, UnfinishedCar, NotUsed> getPaint() {
        return paint;
    }
}
