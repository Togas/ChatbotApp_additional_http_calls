package stucke.chatbot.httpaction.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import stucke.chatbot.httpaction.model.collections.BotConfig;

@Repository
public interface BotConfigRepository extends MongoRepository<BotConfig, String> {

}
