package dynaro.messages;

import dynaro.endpoint.Endpoint;

import java.io.Serializable;

public class RegisterEndpoint
        implements Serializable {

    private Endpoint endpoint;

    private String owner;

    public RegisterEndpoint(Endpoint endpoint, String owner) {
        this.endpoint = endpoint;
        this.owner = owner;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public String getOwner() {
        return owner;
    }
}
