package dynaro.messages.response;

import java.io.Serializable;

public abstract class ServiceResponse
        implements Serializable {

    int status;
    Object body;

    protected ServiceResponse(int status, Object body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public Object getBody() {
        return body;
    }

}
