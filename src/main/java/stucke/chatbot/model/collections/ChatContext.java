package stucke.chatbot.model.collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "ChatContext")
public class ChatContext {

    @Id
    private String id;
    private Map<String, Object> context;

    public ChatContext(){
        this.context =new HashMap<>();
    }
    public ChatContext(String id) {
        this();
        this.id=id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
}
