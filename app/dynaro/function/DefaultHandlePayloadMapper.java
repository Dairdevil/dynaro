package dynaro.function;

import dynaro.messages.gateway.HandlePayload;
import dynaro.messages.ServiceRequest;
import dynaro.microtypes.EndpointPath;

public class DefaultHandlePayloadMapper
        implements SerializableFunction<HandlePayload, ServiceRequest> {

    private String id;

    public DefaultHandlePayloadMapper(String id) {
        this.id = id;
    }

    public ServiceRequest apply(HandlePayload handlePayload) {
        return new ServiceRequest.Builder()
                .withPath(EndpointPath.withValue(id))
                .withPayload(handlePayload.getPayload())
                .withQueryString(handlePayload.getQueryString())
                .build();
    }
}
