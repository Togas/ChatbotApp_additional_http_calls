package stucke.chatbot.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import stucke.chatbot.model.collections.Answer;

public interface AnswerRepository extends MongoRepository<Answer, String> {
}
