package dynaro.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import dynaro.constants.Role;
import dynaro.constants.Topic;
import dynaro.endpoint.Endpoint;
import dynaro.endpoint.EndpointRegistry;
import dynaro.gateway.Gateway;
import dynaro.messages.gateway.HandlePayload;
import dynaro.messages.RegisterEndpoint;
import dynaro.messages.RegisterEndpointConfirmation;
import dynaro.messages.response.failure.EndpointNotFoundResponse;
import dynaro.messages.startup.GatewayJoin;
import dynaro.microtypes.EndpointPath;

public class DynaroSupervisor
        extends AbstractActor {

    public static String NAME = "dynaro-supervisor";

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private Address address;

    @Override
    public Receive createReceive() {

        address = Cluster.get(context().system()).selfAddress();

        ActorRef mediator = DistributedPubSub.get(context().system()).mediator();

        mediator.tell(new DistributedPubSubMediator.Put(getSelf()), getSelf());

        // Notify all services in cluster that new gateway is available
        notifyServices(mediator);

        // Subscribe to member leaving
        Cluster cluster = Cluster.get(getContext().getSystem());
        cluster.registerOnMemberUp(
                () -> cluster.subscribe(getSelf(), ClusterEvent.MemberRemoved.class)
        );

        return receiveBuilder()
                .match(HandlePayload.class, p -> {
                    log.info("HandlePayload received for path %s", p.getPath());

                    Endpoint endpoint = EndpointRegistry.get(EndpointPath.withValue(p.getPath()));

                    if (endpoint == null) {
                        context().sender().tell(new EndpointNotFoundResponse(p.getPath()), getSelf());
                    }
                    else {
                        mediator.tell(
                                new DistributedPubSubMediator.Send(
                                        endpoint.getActorRef(),
                                        endpoint.getMessage(p)),
                                context().sender());
                    }

                })
                .match(RegisterEndpoint.class, r -> {
                    log.info("Received request to register endpoint %s from address %s",
                            r.getEndpoint().getPath(),
                            r.getAddress().toString());

                    EndpointRegistry.put(r);

                    mediator.tell(
                            new DistributedPubSubMediator.SendToAll(
                                    String.format("/user/%s", context().sender().path().name()),
                                    new RegisterEndpointConfirmation(address)),
                            getSelf());

                })
                .match(ClusterEvent.MemberRemoved.class, m -> {

                    // Remove address and possibly endpoint from registry
                    if (m.member().hasRole(Role.SERVICE.getValue())) {
                        EndpointRegistry.purge(m.member().address());
                    }
                })
                .build();
    }

    private void notifyServices(ActorRef mediator) {

        mediator.tell(new DistributedPubSubMediator.Publish(
                Topic.GATEWAY_JOIN.getValue(),
                new GatewayJoin(Gateway.at(address))),
                getSelf());
    }

    public static Props props() {
        return Props.create(DynaroSupervisor.class);
    }
}
