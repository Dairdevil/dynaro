package dynaro.exception.http;

public class ServiceErrorException
        extends HttpStatusException {

    public ServiceErrorException(String message) {
        super(500, message);
    }
}
