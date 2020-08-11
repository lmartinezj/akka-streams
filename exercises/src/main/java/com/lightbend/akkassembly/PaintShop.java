package com.lightbend.akkassembly;

import akka.NotUsed;
import akka.stream.javadsl.Source;

import java.util.Set;

public class PaintShop {
    private final Source<Color, NotUsed> colors;

    PaintShop(Set<Color> colorSet) {
        this.colors = Source.cycle(colorSet::iterator);
    }

    Source<Color, NotUsed> getColors() {
        return colors;
    }
}
