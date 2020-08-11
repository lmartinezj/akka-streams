package com.lightbend.akkassembly;

import akka.Done;
import akka.event.LoggingAdapter;
import akka.stream.javadsl.Sink;

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
}
