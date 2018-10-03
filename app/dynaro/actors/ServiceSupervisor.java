package dynaro.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import dynaro.messages.request.ServiceRequest;
import dynaro.messages.service.EndpointRequest;
import dynaro.messages.service.adapter.Adapter;
import dynaro.messages.service.adapter.AdapterFactory;
import dynaro.messages.service.adapter.DefaultAdapterFactory;

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

        return createReceiveBuilder()
                .match(ServiceRequest.class, r -> {
                    log.info("Service Request message received in Service Supervisor");

                    Adapter adapter = this.adapterFactory.getAdapter(r.getPath());

                    EndpointRequest adapted = adapter.adapt(r);

                    context().actorOf(adapter.getActorProps()).tell(adapted, context().sender());
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
