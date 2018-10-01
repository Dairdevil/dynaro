package dynaro.function;

import dynaro.messages.HandlePayload;
import dynaro.messages.ServiceRequest;

public class DefaultHandlePayloadMapper
        implements SerializableFunction<HandlePayload, ServiceRequest> {

    private String id;

    public DefaultHandlePayloadMapper(String id) {
        this.id = id;
    }

    public ServiceRequest apply(HandlePayload handlePayload) {
        return new ServiceRequest.Builder()
                .withId(id)
                .withPayload(handlePayload.getPayload())
                .withQueryString(handlePayload.getQueryString())
                .build();
    }
}
