package dynaro.controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.management.AkkaManagement;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.pattern.Patterns;
import dynaro.actors.DynaroSupervisor;
import dynaro.messages.gateway.HandlePayload;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;

import java.util.concurrent.CompletionStage;

public abstract class DynaroController
        extends Controller {

    private LoggingAdapter log;

    private ActorSystem system;
    private ActorRef dynaroSupervisor;

    /**
     * Controller initialisation including starting akka management and cluster bootstrapping
     *
     * @param system ActorSystem for the node
     */
    public DynaroController(ActorSystem system) {

        this.system = system;

        this.preInit();

        log = Logging.getLogger(system, this);

        log.info("Starting DynaroController for system %s", system.name());

        // Akka Management hosts the HTTP routes used by bootstrap
        AkkaManagement.get(system).start();
        log.info("Akka Management initialised DynaroController");

        // Starting the bootstrap process needs to be done explicitly
        ClusterBootstrap.get(system).start();
        log.info("Cluster Bootstrap initialised DynaroController");

        dynaroSupervisor = system.actorOf(DynaroSupervisor.props(), DynaroSupervisor.NAME);

        this.postInit();
    }

    /**
     * Optional actions to take before initialising controller.
     */
    protected void preInit() {

    }

    /**
     * Optional actions to take after initialising controller.
     */
    protected void postInit() {

    }

    public CompletionStage<Result> handle(String path) {

        this.preHandle();

        HandlePayload handlePayload = new HandlePayload.Builder()
                .withPath(path)
                .withQueryString(request().queryString())
                .build();

        return FutureConverters.toJava(
                Patterns.ask(
                        dynaroSupervisor,
                        handlePayload,
                        30000))
                .thenApply(response -> {

                    this.postHandle();

                    return ok((String) response);
                });
    }

    /**
     * Optional actions to take before handling a request
     */
    protected void preHandle() {

    }

    /**
     * Optional actions to take after handling a request before returning
     */
    protected void postHandle() {

    }

    public ActorSystem getSystem() {
        return this.system;
    }
}
