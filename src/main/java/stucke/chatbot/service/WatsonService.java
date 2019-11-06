package stucke.chatbot.service;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import org.springframework.stereotype.Service;
import stucke.chatbot.model.collections.BotConfig;
import stucke.chatbot.model.collections.ChatContext;

import java.util.List;

@Service
public class WatsonService {

    public List<String> getAnswersFromWatson(ChatContext context, BotConfig botConfig, String message) {
        InputData inputData = new InputData.Builder().text(message).build();

        Context watsonContext = new Context();
        watsonContext.putAll(context.getContext());

        MessageOptions messageOptions = new MessageOptions.Builder()
                .input(inputData)
                .context(watsonContext)
                .workspaceId(botConfig.getBotId())
                .alternateIntents(true)
                .build();

        Assistant assistant = new Assistant("2019-01-11", botConfig.getUsername(), botConfig.getPassword());
        assistant.setEndPoint(botConfig.getEndpoint());

        MessageResponse watsonResponse = assistant.message(messageOptions).execute();
        context.setContext(watsonResponse.getContext());

        return watsonResponse.getOutput().getText();
    }
}
