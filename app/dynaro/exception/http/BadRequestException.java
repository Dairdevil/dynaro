package dynaro.exception.http;

public class BadRequestException
        extends HttpStatusException {

    public BadRequestException(String message) {
        super(400, message);
    }
}
