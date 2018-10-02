package dynaro.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import dynaro.endpoint.Endpoint;
import dynaro.endpoint.EndpointRegistry;
import dynaro.messages.gateway.HandlePayload;
import dynaro.messages.RegisterEndpoint;
import dynaro.messages.RegisterEndpointConfirmation;
import dynaro.microtypes.EndpointPath;

public class DynaroSupervisor
        extends AbstractActor {

    public static String NAME = "dynaro-supervisor";

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {

        ActorRef mediator = DistributedPubSub.get(context().system()).mediator();

        mediator.tell(new DistributedPubSubMediator.Put(getSelf()), getSelf());

        return receiveBuilder()
                .match(HandlePayload.class, p -> {
                    log.info("HandlePayload received for path %s", p.getPath());

                    Endpoint endpoint = EndpointRegistry.get(EndpointPath.withValue(p.getPath()));

                    if (endpoint == null) {
                        context().sender().tell("Endpoint not found", getSelf());
                    }
                    else {
                        mediator.tell(
                                new DistributedPubSubMediator.Send(
                                        endpoint.getActorRef(),
                                        endpoint.getMessage(p)
                                ), context().sender());
                    }
                })
                .match(RegisterEndpoint.class, r -> {
                    EndpointRegistry.put(r.getEndpoint().getPath(), r.getEndpoint());
                    mediator.tell(
                            new DistributedPubSubMediator.SendToAll(
                                    String.format("/user/%s", context().sender().path().name()),
                                    new RegisterEndpointConfirmation()
                            ), getSelf());
                })
                .build();
    }

    public static Props props() {
        return Props.create(DynaroSupervisor.class);
    }
}
