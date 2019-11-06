package stucke.chatbot.model;

import java.util.Map;

public class HttpAction {

    Request request;
    Map<String, String> response;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Map<String, String> getResponse() {
        return response;
    }

    public void setResponse(Map<String, String> response) {
        this.response = response;
    }
}
