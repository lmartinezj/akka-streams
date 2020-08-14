package com.lightbend.akkassembly;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.event.LoggingAdapter;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import scala.annotation.meta.field;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

class Auditor {
    private final Sink<Car, CompletionStage<Integer>> count;
    //private final Materializer materializer;
    private final ActorSystem system;


    Auditor(Materializer materializer) {
        this.count = Sink.fold(0, (counter, element) -> counter + 1);
        this.system = materializer.system();
        //this.materializer = materializer;
    }


    Auditor(ActorSystem system) {
        this.count = Sink.fold(0, (counter, element) -> counter + 1);
        this.system = system;
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

    CompletionStage<Integer> audit(Source<Car, NotUsed> cars, Duration sampleSize) {
        return cars
                .via(sample(sampleSize))
                .runWith(getCount(), system);
    }
}
