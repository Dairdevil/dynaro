package dynaro.gateway;

import java.util.HashSet;
import java.util.Set;

public class GatewayRegistry {

    public static GatewayRegistry KNOWN_GATEWAYS = new GatewayRegistry();

    private Set<Gateway> gateways;

    public GatewayRegistry() {
        this.gateways = new HashSet<>();
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
