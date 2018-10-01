package dynaro.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import dynaro.messages.ServiceRequest;

public abstract class ServiceSupervisor
        extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {

        return createReceiveBuilder()
                .match(ServiceRequest.class, r -> {
                    log.info("Service Request message received in Service Supervisor");

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
