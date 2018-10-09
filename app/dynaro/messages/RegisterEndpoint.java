package dynaro.messages;

import akka.actor.Address;
import dynaro.endpoint.Endpoint;

import java.io.Serializable;

public class RegisterEndpoint
        implements Serializable {

    private Endpoint endpoint;

    private String owner;

    private Address address;

    public RegisterEndpoint(Endpoint endpoint, String owner, Address address) {
        this.endpoint = endpoint;
        this.owner = owner;
        this.address = address;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public String getOwner() {
        return owner;
    }

    public Address getAddress() {
        return address;
    }
}
