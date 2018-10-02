package dynaro.endpoint;

import dynaro.microtypes.EndpointPath;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndpointRegistry {

    private static EndpointRegistry INSTANCE = new EndpointRegistry();

    private Map<EndpointPath, Endpoint> registry = new HashMap<>();

    public static Endpoint get(EndpointPath key) {
        return INSTANCE.registry.keySet().stream()
                .filter(k -> {
                    Pattern p = Pattern.compile(EndpointPath.valueOf(k));
                    Matcher m = p.matcher(EndpointPath.valueOf(key));
                    return m.matches();
                })
                .map(k -> INSTANCE.registry.get(k))
                .findFirst()
                .orElse(null);
    }

    public static void put(EndpointPath key, Endpoint endpoint) {
        INSTANCE.registry.put(key, endpoint);
    }
}
