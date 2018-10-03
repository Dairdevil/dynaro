package dynaro.messages.request;

import dynaro.microtypes.EndpointPath;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Map;

public class ServiceRequest
        implements Serializable {

    private EndpointPath path;

    private String actualPath;

    private Object payload;

    private transient Map<String, String[]> queryString;

    private ServiceRequest() {

    }

    public EndpointPath getPath() {
        return path;
    }

    public String getActualPath() {
        return actualPath;
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

        public Builder withPath(EndpointPath path) {
            this.serviceRequest.path = path;
            return this;
        }

        public Builder withActualPath(String actualPath) {
            this.serviceRequest.actualPath = actualPath;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ServiceRequest that = (ServiceRequest) o;

        return new EqualsBuilder()
                .append(path, that.path)
                .append(actualPath, that.actualPath)
                .append(payload, that.payload)
                .append(queryString, that.queryString)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(path)
                .append(actualPath)
                .append(payload)
                .append(queryString)
                .toHashCode();
    }
}
