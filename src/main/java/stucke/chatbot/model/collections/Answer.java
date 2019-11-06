package stucke.chatbot.model.collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import stucke.chatbot.model.HttpAction;

@Document(collection = "Answer")
public class Answer {
    @Id
    private String id;
    private String utterance;
    private HttpAction httpAction;

    public Answer() {
    }

    public Answer(String utterance) {
        this.utterance = utterance;
    }

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

    public HttpAction getHttpAction() {
        return httpAction;
    }

    public void setHttpAction(HttpAction httpAction) {
        this.httpAction = httpAction;
    }
}
