package stucke.chatbot.httpaction.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import stucke.chatbot.httpaction.model.Request;

import java.util.Iterator;
import java.util.Map;


@Component
public class HttpCommunicator {

    private static Log logger = LogFactory.getLog(HttpCommunicator.class);

    private WebClient webClient;


    /**
     * @param request
     * @return response of http request
     */
    public Object executeHttpAction(Request request) {
        if (request == null) {
            return null;
        }
        RequestHeadersSpec<?> preparedHttpRequest = prepareHttpCall(request);
        try {
                return preparedHttpRequest.retrieve().bodyToMono(Object.class).block();
        } catch (WebClientResponseException e) {
            logger.warn(e);
        }
        return null;
    }


    private RequestHeadersSpec<?> prepareHttpCall(Request request) {
        String completeUrl = buildUrl(request.getUrl(), request.getQueryParams());
        webClient = WebClient.create(completeUrl);
        WebClient.RequestBodySpec requestBodySpec = webClient.method(request.getHttpMethod());
        if (request.getHeaders() != null) {
            requestBodySpec = requestBodySpec.headers(headers -> headers.setAll(request.getHeaders()));
        }
        if (request.getHttpMethod().equals(HttpMethod.POST) && request.getBody() != null) {
            return requestBodySpec.syncBody(request.getBody());
        }
        return requestBodySpec;
    }

    private String buildUrl(String url, Map<String, String> queryParams) {
        if (queryParams == null) {
            return url;
        }
        StringBuilder completeUrl = new StringBuilder(url + "?");
        Iterator iterator = queryParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry queryParam = (Map.Entry) iterator.next();
            completeUrl.append(queryParam.getKey() + "=" + queryParam.getValue() + "&");
            iterator.remove();
        }
        completeUrl.setLength(completeUrl.length() - 1);
        return completeUrl.toString();
    }
}
