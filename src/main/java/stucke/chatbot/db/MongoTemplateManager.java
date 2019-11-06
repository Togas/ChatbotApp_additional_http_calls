package stucke.chatbot.db;

import com.mongodb.MongoClientURI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * The class organizes the mongodb access. Create an instance of your wished database with createDbManager.
 */
@Service
public class MongoTemplateManager {
    private static Log logger = LogFactory.getLog(MongoTemplateManager.class);

    private final HashMap<String, MongoTemplate> mongoTemplateMap = new HashMap<>();

    /**
     * returns existing instance of {@link MongoTemplate} for chat history or creates it if not exists
     *
     * @param dbUri  of the db you want to access. Note it has to be a valid mongodb link.
     * @return MongoTemplate. Its is a Manager class you can use to acess the wished db.
     */
    public MongoTemplate createDBManager(String dbUri ) {
        if (dbUri == null ) {
            logger.error("dbUri cannot be null.");
            throw new IllegalStateException();
        }
        if (!isValidUri(dbUri)) {
            logger.error("Db URI (" + dbUri + ") is invalid!");
            throw new IllegalArgumentException("Invalid DB URI -> " + dbUri);
        }
        if (!mongoTemplateMap.containsKey(dbUri)) {
            MongoTemplate mongoTemplate = this.createMongoTemplate(dbUri);
            mongoTemplateMap.put(dbUri, mongoTemplate);
        }
        return this.mongoTemplateMap.get(dbUri);
    }

    private MongoTemplate createMongoTemplate(String dbUri) {
        SimpleMongoDbFactory factory = new SimpleMongoDbFactory(new MongoClientURI(dbUri));

        return new MongoTemplate(factory);
    }

    private boolean isValidUri(String dbUri) {
        return !StringUtils.isEmpty(dbUri) && dbUri.startsWith("mongodb://") && !dbUri.startsWith("mongodb+srv://");
    }
}
