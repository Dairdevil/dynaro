package dynaro.exception;

import java.util.UUID;

public abstract class DynaroException
        extends Exception {

    private UUID errorId;

    protected DynaroException(String message) {
        super(message);
        this.errorId = UUID.randomUUID();
    }

    public UUID getErrorId() {
        return errorId;
    }
}
