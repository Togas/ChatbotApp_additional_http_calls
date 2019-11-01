package stucke.chatbot.httpaction.model.collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import stucke.chatbot.httpaction.model.Request;

import java.util.Map;

@Document(collection = "Answer")
public class Answer {
    @Id
    private String id;
    private String utterance;
    private Request request;
    private Map<String, String> response;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUtterance() {
        return utterance;
    }

    public void setUtterance(String utterance) {
        this.utterance = utterance;
    }

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
