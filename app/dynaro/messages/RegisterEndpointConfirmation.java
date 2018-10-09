package dynaro.messages;

import akka.actor.Address;

import java.io.Serializable;

public class RegisterEndpointConfirmation
        implements Serializable {

    private Address gatewayAddress;

    public RegisterEndpointConfirmation(Address gatewayAddress) {
        this.gatewayAddress = gatewayAddress;
    }

    public Address getGatewayAddress() {
        return gatewayAddress;
    }
}
