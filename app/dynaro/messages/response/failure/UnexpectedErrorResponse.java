package dynaro.messages.response.failure;

import dynaro.exception.http.SeverityLevel;

public class UnexpectedErrorResponse
        extends FailureResponse {

    public UnexpectedErrorResponse(Throwable t) {
        super(
                500,
                new FailureResponseBody(
                        SeverityLevel.FATAL,
                        String.format("Unexpected error encountered: %s", t.getMessage()))
        );
    }
}
