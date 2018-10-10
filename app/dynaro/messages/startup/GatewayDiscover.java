package dynaro.messages.startup;

import dynaro.gateway.Gateway;

import java.io.Serializable;

public class GatewayDiscover
        implements Serializable {

    Gateway gateway;

    public GatewayDiscover(Gateway gateway) {
        this.gateway = gateway;
    }

    public Gateway getGateway() {
        return gateway;
    }
}
