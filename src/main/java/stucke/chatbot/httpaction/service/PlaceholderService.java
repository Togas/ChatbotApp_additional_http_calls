package stucke.chatbot.httpaction.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PlaceholderService {

    private static final String PATTERN_KEY = "\\$\\{([^}]*)}";
    private static final String PATTERN_JSON_KEY = "[^.]*";
    private final Pattern jsonPattern = Pattern.compile(PATTERN_JSON_KEY);
    private final Pattern placeholderPattern = Pattern.compile(PATTERN_KEY);
    private Log logger = LogFactory.getLog(PlaceholderService.class);

    /**
     * swaps entity placeholder with actual entity value from custom context
     *
     * @param searchForEntity pass a map , e.g. header map or body map of remote action. The method searches for
     *                        a value marked by @. If @ is present, it searches in customContext for the value and
     *                        replaces it.
     * @param customContext
     * @return
     */
    public void swapEntityPlaceholder(Map<String, String> searchForEntity, Map<String, Object> customContext) {
        if (searchForEntity == null || customContext == null) {
            return;
        }
        Map<String, String> clonedSearchMap = new LinkedHashMap<>(searchForEntity);
        Iterator iterator = clonedSearchMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry searchForEntityPair = (Map.Entry) iterator.next();
            String searchForEntityPairValue = (String) searchForEntityPair.getValue();
            String searchForEntityPairKey = (String) searchForEntityPair.getKey();
            StringBuilder output = new StringBuilder(searchForEntityPairValue.length() * 2);
            Matcher matcher = placeholderPattern.matcher(searchForEntityPairValue);
            int lastEndIndex = 0;
            while (matcher.find()) {
                output.append(searchForEntityPairValue, lastEndIndex, matcher.start());
                lastEndIndex = matcher.end();
                String key = matcher.group(1);
                String conversationValue = findInContext(customContext, key).toString();

                if (conversationValue == null) {
                    logger.info("Placeholder value: " + searchForEntityPairValue + " was not found for key: "
                            + searchForEntityPairKey);
                }
                searchForEntity.put(searchForEntityPairKey, conversationValue);
            }
            iterator.remove();
        }
    }
    public String insertPlaceholders(String text, Map<String, Object> context) {
        if (text == null) {
            return null;
        }
        StringBuilder output = new StringBuilder(text.length() * 2);
        Matcher matcher = placeholderPattern.matcher(text);
        int lastEndIndex = 0;
        while (matcher.find()) {
            output.append(text, lastEndIndex, matcher.start());
            lastEndIndex = matcher.end();
            String key = matcher.group(1);
            Object conversationValue = findInContext(context, key);

            String replacement="";
            if (conversationValue != null) {
                replacement = conversationValue.toString();
            }
            output.append(replacement);
        }
        output.append(text.substring(lastEndIndex));
        return output.toString();
    }
    /**
     * Method puts the fitting httpResponse in customContext
     *
     * @param httpResponse     actual httpResponse
     * @param responseInAction defines which responses should be saved to context. The key is saved to customContext.
     *                         As value it replaces the value from responseAction with the value in httpResponse.
     *                         E.g. if responseInAction is  temperature:main.temp it searches for object main in httpResponse
     *                         and takes the attribute temp as value. If main.temp has value 20, it puts the key-value:
     *                         temperature-20 to context.
     * @param customContext
     */
    public void putHttpResponseInContext(Map<String, Object> httpResponse, Map<String, String> responseInAction, Map<String, Object> customContext) {
        if (httpResponse == null || responseInAction == null || customContext == null) {
            return;
        }
        Map<String, String> responseInActionClone = new LinkedHashMap<>(responseInAction);
        Iterator iterator = responseInActionClone.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            String value = (String) pair.getValue();
            String key = (String) pair.getKey();
            Object httpResponseValue = findInContext(httpResponse, value);
            customContext.put(key, httpResponseValue);
            iterator.remove();
        }
    }

    /**
     * This method searches the passed context for a specific key.
     * E.g. if you have a nested map and a  key with main.temp,
     * it goes in the key "main" and then it goes to attribute "temp".
     *
     * @param context The context object with all variables
     * @param key     the complete key statement like username or
     *                main.temp
     * @return An Object, can be String, map...
     */
    @SuppressWarnings("rawtypes")
    public Object findInContext(Map<String, Object> context, String key) {

        if (!key.contains(".")) {
            return context.get(key);
        }

        Matcher matcher = jsonPattern.matcher(key);
        Map level = context;
        while (matcher.find()) {

            String partKey = matcher.group(0);

            if (partKey.isEmpty()) {
                continue;
            }

            if (!level.containsKey(partKey)) {
                return null;
            }

            Object value = level.get(partKey);

            if (value instanceof Map) {
                level = (Map) value;
            } else {
                return value;
            }
        }
        return null;

    }

}
