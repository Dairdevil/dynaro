package dynaro.messages.service.adapter;

import akka.actor.Props;
import dynaro.actors.EndpointWorker;
import dynaro.messages.request.ServiceRequest;
import dynaro.messages.service.EndpointRequest;

public interface Adapter<R extends EndpointRequest, W extends EndpointWorker<R>> {

    R adapt(ServiceRequest r);

    Props getActorProps();
}
