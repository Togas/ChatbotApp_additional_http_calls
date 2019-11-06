package stucke.chatbot.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import stucke.chatbot.model.collections.ChatContext;

@Repository
public interface ChatContextRepository extends MongoRepository<ChatContext, String> {
}
