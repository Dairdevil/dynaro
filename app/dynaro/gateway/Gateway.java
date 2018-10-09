package dynaro.gateway;

import akka.actor.Address;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class Gateway
        implements Serializable {

    private Address address;

    private Gateway(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public static Gateway at(Address address) {
        return new Gateway(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Gateway gateway = (Gateway) o;

        return new EqualsBuilder()
                .append(address, gateway.address)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(address)
                .toHashCode();
    }
}
