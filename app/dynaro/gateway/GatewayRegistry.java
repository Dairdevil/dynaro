package dynaro.gateway;

import java.util.ArrayList;
import java.util.Collection;

public class GatewayRegistry {

    public static GatewayRegistry KNOWN_GATEWAYS = new GatewayRegistry();

    private Collection<Gateway> gateways;

    public GatewayRegistry() {
        this.gateways = new ArrayList<>();
    }

    public void add(Gateway gateway) {
        this.gateways.add(gateway);
    }

    public void remove(Gateway gateway) {
        this.gateways.remove(gateway);
    }

    public boolean isEmpty() {
        return this.gateways == null
                || this.gateways.size() == 0;
    }

    public boolean containsAdditionalGateways(GatewayRegistry comparisonRegistry) {
        return this.gateways.size() > comparisonRegistry.gateways.size()
                && this.gateways.containsAll(comparisonRegistry.gateways);
    }
}
