package dynaro.messages.response.failure;

import dynaro.exception.DynaroException;
import dynaro.exception.http.HttpStatusException;
import dynaro.messages.response.ServiceResponse;

public class FailureResponse
        extends ServiceResponse {

    public FailureResponse(HttpStatusException hse) {
        super(hse.getStatus(), new FailureResponseBody(hse));
    }

    public FailureResponse(DynaroException de) {
        super(500, new FailureResponseBody(de));
    }

    public FailureResponse(int status, FailureResponseBody failureResponseBody) {
        super(status, failureResponseBody);
    }
}
