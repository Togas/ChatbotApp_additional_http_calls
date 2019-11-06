package stucke.chatbot.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import stucke.chatbot.db.MongoTemplateManager;
import stucke.chatbot.model.collections.Answer;
import stucke.chatbot.model.collections.BotConfig;
import stucke.chatbot.model.collections.ChatContext;

import javax.annotation.PostConstruct;

@Service
public class DatabaseService {
    private static Log logger = LogFactory.getLog(DatabaseService.class);

    @Autowired
    MongoTemplateManager templateManager;

    MongoTemplate template;

    @PostConstruct
    public void init() {
        template = templateManager.createDBManager("mongodb://localhost/praxisbericht_db");
    }


    public Answer getAnswerById(String id) {
        Answer answer = template.findById(id, Answer.class);
        if (answer == null) {
            logger.warn("No Answer found for for answer id: " + id);
        }
        return answer;
    }

    public BotConfig getBotConfig(String id) {
        BotConfig botConfig = template.findById(id, BotConfig.class);
        if (botConfig == null) {
            logger.warn("No botConfig foudn for botConfig id: " + id);
        }
        return botConfig;
    }

    public ChatContext getChatContext(String id) {
        ChatContext chatContext = template.findById(id, ChatContext.class);
        if (chatContext == null) {
            logger.warn("No ChatContext found, so new ChatContext is created for id: " + id);
            chatContext = new ChatContext(id);
            template.save(chatContext);
        }
        return chatContext;
    }

    public void saveChatContext(ChatContext context) {
        template.save(context);
    }

}
