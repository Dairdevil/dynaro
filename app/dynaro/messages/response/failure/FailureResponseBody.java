package dynaro.messages.response.failure;

import dynaro.exception.DynaroException;
import dynaro.exception.http.HttpStatusException;
import dynaro.exception.http.SeverityLevel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class FailureResponseBody
        implements Serializable {

    static DateFormat df;

    {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
    }

    private UUID errorId;
    private SeverityLevel severity;
    private String message;
    private String timestamp;

    public FailureResponseBody(HttpStatusException hse) {

        this.errorId = hse.getErrorId();
        this.severity = hse.getSeverity();
        this.message = hse.getMessage();
        this.timestamp = df.format(new Date());
    }

    public FailureResponseBody(DynaroException de) {

        this.errorId = de.getErrorId();
        this.severity = SeverityLevel.FATAL;
        this.message = de.getMessage();
        this.timestamp = df.format(new Date());
    }

    public FailureResponseBody(SeverityLevel severity, String message) {

        this.errorId = UUID.randomUUID();
        this.severity = severity;
        this.message = message;
        this.timestamp = df.format(new Date());
    }

    public UUID getErrorId() {
        return errorId;
    }

    public SeverityLevel getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
