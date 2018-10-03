package dynaro.messages.response.success;

import dynaro.messages.response.ServiceResponse;

public class SuccessResponse
        extends ServiceResponse {

    public SuccessResponse(Object body) {
        super(200, body);
    }
}
