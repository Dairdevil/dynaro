package dynaro.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Address;
import akka.cluster.Cluster;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import dynaro.endpoint.Endpoint;
import dynaro.exception.DynaroException;
import dynaro.exception.http.HttpStatusException;
import dynaro.gateway.Gateway;
import dynaro.gateway.GatewayRegistry;
import dynaro.messages.RegisterEndpoint;
import dynaro.messages.RegisterEndpointConfirmation;
import dynaro.messages.response.ServiceResponse;
import dynaro.messages.response.failure.FailureResponse;
import dynaro.messages.response.failure.UnexpectedErrorResponse;
import dynaro.messages.response.success.SuccessResponse;
import dynaro.messages.service.CheckKnownGateways;
import dynaro.messages.service.EndpointRequest;

import java.lang.reflect.ParameterizedType;
import java.time.Duration;

import static dynaro.gateway.GatewayRegistry.KNOWN_GATEWAYS;

public abstract class EndpointWorker<R extends EndpointRequest>
        extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ActorRef mediator = DistributedPubSub.get(getContext().getSystem()).mediator();

    private static GatewayRegistry REGISTERED_GATEWAYS = new GatewayRegistry();

    private Address address;

    private Long scheduleInterval = 10L;

    @Override
    public Receive createReceive() {

        log.debug("Determining node address");
        address = Cluster.get(context().system()).selfAddress();

        log.debug("Registering self with mediator");
        mediator.tell(new DistributedPubSubMediator.Put(getSelf()), getSelf());

        scheduleCheckKnownGateways();

        return receiveBuilder()
                .match(getRequestClass(), r -> {
                    log.info("Message received in Dynaro Endpoint Worker");

                    ServiceResponse response;

                    try {
                        Object obj = performAction(r);
                        response = new SuccessResponse(obj);
                    }
                    catch (HttpStatusException hse) {
                        response = new FailureResponse(hse);
                    }
                    catch (DynaroException de) {
                        response = new FailureResponse(de);
                    }
                    catch (Throwable t) {
                        response = new UnexpectedErrorResponse(t);
                    }

                    context().sender().tell(response, getSelf());

                })
                .match(RegisterEndpoint.class, r -> {
                    if (!registeredOnAllGateways()) {
                        log.debug("Attempting to register self with gateways");

                        // TODO find gateways not yet registered and target these rather than sendtoall
                        mediator.tell(
                                new DistributedPubSubMediator.SendToAll(
                                        String.format("/user/%s", DynaroSupervisor.NAME),
                                        r),
                                getSelf());

                        scheduleRegisterEndpoint();
                    }
                    else {
                        log.debug("Self already registered with all known gateways, ending scheduling loop");
                    }

                })
                .match(RegisterEndpointConfirmation.class, r -> {
                    log.debug("Successfully registered self with gateway");

                    REGISTERED_GATEWAYS.add(Gateway.at(r.getGatewayAddress()));
                })
                .match(CheckKnownGateways.class, r -> {
                    log.debug("Checking registered gateways against known list");

                    // check for any known gateways not currently registered against
                    if (KNOWN_GATEWAYS.containsAdditionalGateways(REGISTERED_GATEWAYS)) {
                        log.debug("Detected previously unknown gateway(s), beginning registration loop");
                        scheduleRegisterEndpoint();
                    }

                    // check for any gateways registered against that no longer exist
                    if (REGISTERED_GATEWAYS.containsAdditionalGateways(KNOWN_GATEWAYS)) {
                        log.debug("Obsolete gateways found, removing from list");
                        REGISTERED_GATEWAYS = KNOWN_GATEWAYS;

                        if (REGISTERED_GATEWAYS.isEmpty()) {
                            log.debug("No known gateways remaining, will attempt to register once new gateways are found");
                        }
                    }

                    // schedule another check
                    scheduleCheckKnownGateways();
                })
                .build();
    }

    protected abstract Endpoint getEndpoint();

    protected abstract Object performAction(R request) throws DynaroException;

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
                        new RegisterEndpoint(getEndpoint(), getSupervisorName(), address),
                        getContext().dispatcher(),
                        getSelf());
    }

    private void scheduleCheckKnownGateways() {

        getContext().getSystem().scheduler()
                .scheduleOnce(
                        Duration.ofSeconds(6 * scheduleInterval),
                        getSelf(),
                        new CheckKnownGateways(),
                        getContext().dispatcher(),
                        getSelf());
    }

    private boolean registeredOnAllGateways() {
        return !KNOWN_GATEWAYS.containsAdditionalGateways(REGISTERED_GATEWAYS);
    }
}
