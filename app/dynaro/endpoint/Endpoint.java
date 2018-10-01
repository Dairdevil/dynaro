package dynaro.endpoint;

import java.io.Serializable;

public interface Endpoint<I, O>
        extends Serializable {

    String getPath();

    String getActorRef();

    Object getMessage(I input);

    Class<O> returnType();
}
