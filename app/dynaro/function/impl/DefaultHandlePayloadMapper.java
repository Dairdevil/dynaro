package dynaro.function.impl;

import dynaro.function.SerializableFunction;
import dynaro.messages.gateway.HandlePayload;
import dynaro.messages.request.ServiceRequest;
import dynaro.microtypes.EndpointPath;

import java.util.HashMap;

public class DefaultHandlePayloadMapper
        implements SerializableFunction<HandlePayload, ServiceRequest> {

    private String path;

    public DefaultHandlePayloadMapper(String path) {
        this.path = path;
    }

    public ServiceRequest apply(HandlePayload handlePayload) {
        return new ServiceRequest.Builder()
                .withPath(EndpointPath.withValue(path))
                .withActualPath(handlePayload.getPath())
                .withPayload(handlePayload.getPayload())
                .withQueryString(handlePayload.getQueryString() == null ? new HashMap<>() : handlePayload.getQueryString())
                .build();
    }
}
