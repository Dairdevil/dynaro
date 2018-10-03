package dynaro.messages.response.failure;

import dynaro.exception.http.SeverityLevel;

public class EndpointNotFoundResponse
        extends FailureResponse {

    public EndpointNotFoundResponse(String endpoint) {
        super(
                404,
                new FailureResponseBody(
                        SeverityLevel.WARN,
                        String.format("Endpoint %s not found in application", endpoint))
        );
    }
}
