package stucke.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import stucke.chatbot.model.Request;
import stucke.chatbot.model.collections.Answer;
import stucke.chatbot.model.collections.BotConfig;
import stucke.chatbot.model.collections.ChatContext;
import stucke.chatbot.service.DatabaseService;
import stucke.chatbot.service.HttpCommunicator;
import stucke.chatbot.service.PlaceholderService;
import stucke.chatbot.service.WatsonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
public class ChatServerController {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    WatsonService watsonService;

    @Autowired
    PlaceholderService placeholderService;

    @Autowired
    HttpCommunicator httpCommunicator;

    @CrossOrigin
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public List<String> processChatMessage(@RequestParam("message") String message,
                                           @RequestParam("chatContextId") String chatContextId,
                                           @RequestParam("botId") String botId) {
        BotConfig botConfig = databaseService.getBotConfig(botId);
        ChatContext chatContext = databaseService.getChatContext(chatContextId);
        List<String> watsonAnswers = watsonService.getAnswersFromWatson(chatContext, botConfig, message);

        List<Answer> answers = getAnswersFromDB(watsonAnswers);
        processHttpActions(answers, chatContext.getContext());
        databaseService.saveChatContext(chatContext);

        List<String> completeAnswers = answers.stream().map(Answer::getUtterance).collect(Collectors.toList());

        return completeAnswers;
    }

    private List<Answer> getAnswersFromDB(List<String> watsonAnswers) {
        List<Answer> answers = new ArrayList<>();
        watsonAnswers.forEach(watsonAnswer -> {
            Answer answer = databaseService.getAnswerById(watsonAnswer);
            if (answer == null) {
                answer = new Answer(watsonAnswer);
            }
            answers.add(answer);
        });
        return answers;
    }

    private void processHttpActions(List<Answer> answers, Map<String, Object> context) {
        answers.forEach(answer -> {
            if (answer.getHttpAction() == null || answer.getHttpAction().getRequest() == null) {
                return;
            }
            Request request = answer.getHttpAction().getRequest();
            Map<String, String> httpActionHeader = request.getHeaders();
            Map<String, String> httpActionQueryParams = request.getQueryParams();
            Map<String, String> httpActionBody = request.getBody();

            String replacedUrl = placeholderService.insertPlaceholders(request.getUrl(), context);
            request.setUrl(replacedUrl);

            placeholderService.swapEntityPlaceholder(httpActionHeader, context);
            placeholderService.swapEntityPlaceholder(httpActionQueryParams, context);
            placeholderService.swapEntityPlaceholder(httpActionBody, context);

            Object response = httpCommunicator.executeHttpAction(request);
            if (response instanceof Map) {
                placeholderService.putHttpResponseInContext((Map) response, answer.getHttpAction().getResponse(), context);
            }

            String replacedAnswer = placeholderService.insertPlaceholders(answer.getUtterance(), context);
            answer.setUtterance(replacedAnswer);
            context.remove("city");
        });
    }


}
