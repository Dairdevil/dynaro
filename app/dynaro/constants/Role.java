package dynaro.constants;

public enum Role {

    GATEWAY("dynaro-gateway"),
    SERVICE("dynaro-service");


    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
