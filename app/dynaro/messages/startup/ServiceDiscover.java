package dynaro.messages.startup;

import java.io.Serializable;

public class ServiceDiscover
        implements Serializable {

    private String supervisorName;

    public ServiceDiscover(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorName() {
        return supervisorName;
    }
}
