package dynaro.messages.startup;

import dynaro.gateway.Gateway;

import java.io.Serializable;

public class GatewayJoin
        implements Serializable {

    Gateway gateway;

    public GatewayJoin(Gateway gateway) {
        this.gateway = gateway;
    }

    public Gateway getGateway() {
        return gateway;
    }
}
