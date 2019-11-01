package stucke.chatbot.httpaction.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import stucke.chatbot.httpaction.model.collections.Answer;

public interface AnswerRepository extends MongoRepository<Answer, String> {
}
