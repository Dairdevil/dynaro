package dynaro.constants;

public enum Topic {

    GATEWAY_JOIN("gateway-join"),
    GATEWAY_LEAVE("gateway-leave"),
    SERVICE_JOIN("service-join"),
    SERVICE_LEAVE("service-leave");

    private String value;

    Topic(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
