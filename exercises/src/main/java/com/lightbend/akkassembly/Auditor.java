package com.lightbend.akkassembly;

import akka.Done;
import akka.NotUsed;
import akka.event.LoggingAdapter;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

class Auditor {
    private final Sink<Car, CompletionStage<Integer>> count;

    Auditor() {
        this.count = Sink.fold(0, (counter, element) -> counter + 1);
    }

    Sink<Car, CompletionStage<Integer>> getCount() {
        return count;
    }

    Sink<Object, CompletionStage<Done>> log(LoggingAdapter loggingAdapter) {
        return Sink.foreach(element -> loggingAdapter.debug(element.toString()));
    }

    Flow<Car, Car, NotUsed> sample(Duration sampleSize) {
        return Flow.of(Car.class).takeWithin(sampleSize);
    }
}
