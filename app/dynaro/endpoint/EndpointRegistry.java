package dynaro.endpoint;

import akka.actor.Address;
import dynaro.messages.RegisterEndpoint;
import dynaro.microtypes.EndpointPath;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndpointRegistry {

    private static EndpointRegistry INSTANCE = new EndpointRegistry();

    private Map<EndpointPath, Endpoint> pathToEndpointRegistry = new HashMap<>();

    private Map<EndpointPath, Set<Address>> pathToAddressesRegistry = new HashMap<>();

    public static Endpoint get(EndpointPath key) {
        return INSTANCE.pathToEndpointRegistry.keySet().stream()
                .filter(k -> {
                    Pattern p = Pattern.compile(EndpointPath.valueOf(k));
                    Matcher m = p.matcher(EndpointPath.valueOf(key));
                    return m.matches();
                })
                .map(k -> INSTANCE.pathToEndpointRegistry.get(k))
                .findFirst()
                .orElse(null);
    }

    public static void put(EndpointPath key, Endpoint endpoint, Address address) {
        INSTANCE.pathToEndpointRegistry.put(key, endpoint);

        Set<Address> currentAddressesForPaths = INSTANCE.pathToAddressesRegistry.get(key);

        if (currentAddressesForPaths == null) {
            INSTANCE.pathToAddressesRegistry.put(key, new HashSet<Address>(){{add(address);}});
        }
        else {
            currentAddressesForPaths.add(address);
            INSTANCE.pathToAddressesRegistry.put(key, currentAddressesForPaths);
        }
    }

    public static void put(RegisterEndpoint r) {

        EndpointPath key = r.getEndpoint().getPath();
        Endpoint endpoint = r.getEndpoint();
        Address address = r.getAddress();

        put(key, endpoint, address);
    }

    public static void purge(Address address) {

        Set<EndpointPath> pathsToRemove = new HashSet<>();

        INSTANCE.pathToAddressesRegistry.forEach((p, col) -> {
            if (col.contains(address)) {
                col.remove(address);

                // if address being purged was last one for that endpoint, remove from the registry
                if (col.size() == 0) {
                    pathsToRemove.add(p);
                }
            }
        });

        if (pathsToRemove.size() > 0) {
            pathsToRemove.forEach(p -> {
                INSTANCE.pathToEndpointRegistry.remove(p);
                INSTANCE.pathToAddressesRegistry.remove(p);
            });
        }
    }
}
