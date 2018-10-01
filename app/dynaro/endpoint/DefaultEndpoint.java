package dynaro.endpoint;

import dynaro.function.SerializableFunction;
import dynaro.microtypes.EndpointPath;

public class DefaultEndpoint<I, O>
        implements Endpoint<I, O> {

    private String actorRef;
    private SerializableFunction<I, Object> mapToMessage;
    private EndpointPath path;

    public DefaultEndpoint(EndpointPath path, SerializableFunction<I, Object> mapToMessage, String actorRef) {
        this.path = path;
        this.mapToMessage = mapToMessage;
        this.actorRef = actorRef;
    }

    public String getActorRef() {
        return actorRef;
    }

    public Object getMessage(I input) {
        return mapToMessage.apply(input);
    }

    public EndpointPath getPath() {
        return path;
    }

    // TODO
    public Class<O> returnType() {
        return null;
    }
}
