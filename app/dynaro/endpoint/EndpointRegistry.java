package dynaro.endpoint;

import dynaro.microtypes.EndpointPath;

import java.util.HashMap;
import java.util.Map;

public class EndpointRegistry {

    private static EndpointRegistry INSTANCE = new EndpointRegistry();

    private Map<EndpointPath, Endpoint> registry = new HashMap<>();

    public static Endpoint get(EndpointPath key) {
        return INSTANCE.registry.get(key);
    }

    public static void put(EndpointPath key, Endpoint endpoint) {
        INSTANCE.registry.put(key, endpoint);
    }
}
