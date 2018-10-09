package dynaro.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import dynaro.constants.Role;
import dynaro.constants.Topic;
import dynaro.gateway.Gateway;
import dynaro.messages.request.ServiceRequest;
import dynaro.messages.service.EndpointRequest;
import dynaro.messages.service.adapter.Adapter;
import dynaro.messages.service.adapter.AdapterFactory;
import dynaro.messages.service.adapter.DefaultAdapterFactory;
import dynaro.messages.startup.GatewayJoin;

import static dynaro.gateway.GatewayRegistry.KNOWN_GATEWAYS;

public abstract class ServiceSupervisor
        extends AbstractActor {

    // TODO Resolve use of Google Guice
    // @Inject
    private AdapterFactory adapterFactory = DefaultAdapterFactory.getInstance();

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {

        ActorRef mediator = DistributedPubSub.get(context().system()).mediator();

        mediator.tell(new DistributedPubSubMediator.Put(getSelf()), getSelf());

        // Subscribe to gateway joining
        mediator.tell(new DistributedPubSubMediator.Subscribe(Topic.GATEWAY_JOIN.getValue(), getSelf()), getSelf());

        // Subscribe to member leaving
        Cluster cluster = Cluster.get(getContext().getSystem());
        cluster.registerOnMemberUp(
                () -> cluster.subscribe(getSelf(), ClusterEvent.MemberRemoved.class)
        );

        return createReceiveBuilder()
                .match(ServiceRequest.class, r -> {
                    log.info("Service Request message received in Service Supervisor");

                    Adapter adapter = this.adapterFactory.getAdapter(r.getPath());

                    EndpointRequest adapted = adapter.adapt(r);

                    context().actorOf(adapter.getActorProps()).tell(adapted, context().sender());
                })
                .match(GatewayJoin.class, g -> {
                    log.info("New gateway detected with address %s", g.getGateway().getAddress());

                    // Add gateway to known gateways, service actors will detect this change and update accordingly
                    KNOWN_GATEWAYS.add(g.getGateway());
                })
                .match(ClusterEvent.MemberRemoved.class, m -> {

                    // Remove gateway from known gateways, service actors will detect this change and update accordingly
                    if (m.member().hasRole(Role.GATEWAY.getValue())) {
                        KNOWN_GATEWAYS.remove(Gateway.at(m.member().address()));
                    }
                })
                .match(DistributedPubSubMediator.SubscribeAck.class, ack -> {
                    log.info("Successfully subscribed to a topic");
                })
                .build();
    }

    /**
     * Default implementation returns default receiveBuilder(), can be overridden as necessary
     *
     * @return ReceiveBuilder
     */
    protected ReceiveBuilder createReceiveBuilder() {
        return receiveBuilder();
    }
}
