package com.lightbend.akkassembly;

import akka.stream.javadsl.Sink;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import static org.junit.Assert.*;

public class PaintShopTest extends AkkaSpec {

    @Test
    public void colors_shouldRepeatEachColorInTheColorSet() {
        Set<Color> colorSet = new HashSet<>();
        colorSet.add(Color.fromHex("FFFFFF"));
        colorSet.add(Color.fromHex("000000"));
        colorSet.add(Color.fromHex("FF00FF"));

        PaintShop paintShop = new PaintShop(colorSet);

        List<Color> colors = paintShop.getColors()
                .take(colorSet.size() * 2)
                .runWith(Sink.seq(), system)
                .toCompletableFuture()
                .join();

        List<Color> expected = new Vector<>(colorSet);
        expected.addAll(colorSet);

        assertEquals(expected.size(), colors.size());
        assertArrayEquals(expected.toArray(), colors.toArray());
    }
}
