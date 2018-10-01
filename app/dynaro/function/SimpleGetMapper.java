package dynaro.function;

import dynaro.messages.ServiceRequest;
import dynaro.microtypes.EndpointPath;

public class SimpleGetMapper
        implements SerializableFunction<Object, ServiceRequest> {

    private String id;

    public SimpleGetMapper(String id) {
        this.id = id;
    }

    public ServiceRequest apply(Object obj) {
        return new ServiceRequest.Builder()
                .withPath(EndpointPath.withValue(id))
                .build();
    }
}
