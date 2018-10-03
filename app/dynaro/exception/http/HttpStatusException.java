package dynaro.exception.http;

import dynaro.exception.DynaroException;

import static dynaro.exception.http.SeverityLevel.ERROR;
import static dynaro.exception.http.SeverityLevel.FATAL;

public abstract class HttpStatusException
        extends DynaroException {

    private int status;
    private SeverityLevel severity;

    protected HttpStatusException(int status, SeverityLevel severity, String message) {
        super(message);
        this.status = status;
        this.severity = severity;
    }

    protected HttpStatusException(int status, String message) {
        super(message);
        this.status = status;
        this.severity = ERROR;
    }

    protected HttpStatusException(int status) {
        super("An unknown error has occurred");
        this.status = status;
        this.severity = ERROR;
    }

    protected HttpStatusException(String message) {
        super(message);
        this.status = 500;
        this.severity = FATAL;
    }

    public int getStatus() {
        return status;
    }

    public SeverityLevel getSeverity() {
        return severity;
    }
}
