package dynaro.actors;

import akka.actor.ActorSystem;
import akka.management.AkkaManagement;
import akka.management.cluster.bootstrap.ClusterBootstrap;

public abstract class ServiceActorSystem {

    protected static void initalise(ActorSystem system) {

        // Akka Management hosts the HTTP routes used by bootstrap
        AkkaManagement.get(system).start();

        // Starting the bootstrap process needs to be done explicitly
        ClusterBootstrap.get(system).start();
    }
}
