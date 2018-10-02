package dynaro.function;

import dynaro.messages.ServiceRequest;
import dynaro.microtypes.EndpointPath;

public class SimpleGetMapper
        implements SerializableFunction<Object, ServiceRequest> {

    private String path;

    public SimpleGetMapper(String path) {
        this.path = path;
    }

    public ServiceRequest apply(Object obj) {
        return new ServiceRequest.Builder()
                .withPath(EndpointPath.withValue(path))
                .build();
    }
}
