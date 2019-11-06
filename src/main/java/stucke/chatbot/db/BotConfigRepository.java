package stucke.chatbot.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import stucke.chatbot.model.collections.BotConfig;

@Repository
public interface BotConfigRepository extends MongoRepository<BotConfig, String> {

}
