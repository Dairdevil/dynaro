package dynaro.messages;

import java.util.Map;

public class HandlePayload {

    String path;
    Object payload;
    Map<String, String[]> queryString;

    private HandlePayload() {

    }

    public String getPath() {
        return path;
    }

    public Object getPayload() {
        return payload;
    }

    public Map<String, String[]> getQueryString() {
        return queryString;
    }

    public static class Builder {

        private HandlePayload handlePayload;

        public Builder() {
            handlePayload = new HandlePayload();
        }

        public Builder withPath(String path) {
            this.handlePayload.path = path;
            return this;
        }

        public Builder withPayload(Object payload) {
            this.handlePayload.payload = payload;
            return this;
        }

        public Builder withQueryString(Map<String, String[]> queryString) {
            this.handlePayload.queryString = queryString;
            return this;
        }

        public HandlePayload build() {
            return this.handlePayload;
        }
    }
}
