package dynaro.microtypes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class EndpointPath
        implements Serializable {

    private String path;

    private EndpointPath(String path) {
        this.path = path;
    }

    public static EndpointPath withValue(String path) {
        return new EndpointPath(path);
    }

    public static String valueOf(EndpointPath endpointPath) {
        return endpointPath == null ? null : endpointPath.path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EndpointPath that = (EndpointPath) o;

        return new EqualsBuilder()
                .append(path, that.path)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(path)
                .toHashCode();
    }
}
