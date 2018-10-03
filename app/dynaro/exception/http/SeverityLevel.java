package dynaro.exception.http;

public enum SeverityLevel {

    INFO("info"),
    WARN("warn"),
    ERROR("error"),
    FATAL("fatal");

    private String value;

    SeverityLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
