package dynaro.endpoint;

import java.util.HashMap;
import java.util.Map;

public class EndpointRegistry {

    private static EndpointRegistry INSTANCE = new EndpointRegistry();

    private Map<String, Endpoint> registry = new HashMap<>();

    public static Endpoint get(String key) {
        return INSTANCE.registry.get(key);
    }

    public static void put(String key, Endpoint endpoint) {
        INSTANCE.registry.put(key, endpoint);
    }
}
