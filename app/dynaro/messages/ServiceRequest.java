package dynaro.messages;

import java.io.Serializable;
import java.util.Map;

public class ServiceRequest
        implements Serializable {

    private String id;

    private Object payload;

    private Map<String, String[]> queryString;

    private ServiceRequest() {

    }

    public String getId() {
        return id;
    }

    public Object getPayload() {
        return payload;
    }

    public Map<String, String[]> getQueryString() {
        return queryString;
    }

    public static class Builder {

        private ServiceRequest serviceRequest;

        public Builder() {
            this.serviceRequest = new ServiceRequest();
        }

        public Builder withId(String id) {
            this.serviceRequest.id = id;
            return this;
        }

        public Builder withPayload(Object payload) {
            this.serviceRequest.payload = payload;
            return this;
        }

        public Builder withQueryString(Map<String, String[]> queryString) {
            this.serviceRequest.queryString = queryString;
            return this;
        }

        public ServiceRequest build() {
            return this.serviceRequest;
        }
    }
}
