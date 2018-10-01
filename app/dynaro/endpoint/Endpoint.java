package dynaro.endpoint;

import dynaro.microtypes.EndpointPath;

import java.io.Serializable;

public interface Endpoint<I, O>
        extends Serializable {

    EndpointPath getPath();

    String getActorRef();

    Object getMessage(I input);

    Class<O> returnType();
}
