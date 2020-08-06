package com.lightbend.akkassembly;

import akka.actor.ActorSystem;
import org.junit.After;
import org.junit.Before;

public class AkkaSpec {

    ActorSystem system;

    @Before
    public void setup() {
        system = ActorSystem.create();
    }

    @After
    public void teardown() {
        system.terminate();
    }
}
