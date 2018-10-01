package dynaro.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import dynaro.endpoint.Endpoint;
import dynaro.messages.RegisterEndpoint;
import dynaro.messages.RegisterEndpointConfirmation;
import dynaro.messages.service.EndpointRequest;

import java.lang.reflect.ParameterizedType;
import java.time.Duration;

public abstract class EndpointWorker<R extends EndpointRequest>
        extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ActorRef mediator = DistributedPubSub.get(getContext().getSystem()).mediator();

    protected boolean registered = false;

    private Long scheduleInterval = 10L;

    @Override
    public Receive createReceive() {

        log.debug("Registering self with mediator");
        mediator.tell(new DistributedPubSubMediator.Put(getSelf()), getSelf());

        scheduleRegisterEndpoint();

        return receiveBuilder()
                .match(getRequestClass(), r -> {
                    log.info("Message received in Dynaro Endpoint Worker");
                    performAction(r);

                })
                .match(RegisterEndpoint.class, r -> {
                    if (!registered) {
                        log.debug("Attempting to register self with gateway");

                        mediator.tell(
                                new DistributedPubSubMediator
                                        .SendToAll(String.format("/user/%s", DynaroSupervisor.NAME), r),
                                getSelf());

                        scheduleRegisterEndpoint();
                    }
                    else {
                        log.debug("Self already registered, ending scheduling loop");
                    }

                })
                .match(RegisterEndpointConfirmation.class, r -> {
                    log.debug("Successfully registered self with gateway");
                    registered = true;

                })
                .build();
    }

    protected abstract Endpoint getEndpoint();

    protected abstract void performAction(R request);

    protected abstract String getSupervisorName();

    protected LoggingAdapter log() {
        return log;
    }

    private Class<R> getRequestClass() {
        return ((Class<R>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    private void scheduleRegisterEndpoint() {

        getContext().getSystem().scheduler()
                .scheduleOnce(
                        Duration.ofSeconds(scheduleInterval),
                        getSelf(),
                        new RegisterEndpoint(getEndpoint(), getSupervisorName()),
                        getContext().dispatcher(),
                        getSelf());
    }
}
