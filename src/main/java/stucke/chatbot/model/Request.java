package stucke.chatbot.model;

import org.springframework.http.HttpMethod;

import java.util.Map;

public class Request {
    private HttpMethod httpMethod;
    private String url;
    private Map<String, String> queryParams;
    private Map<String, String> headers;
    private Map<String, String> body;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Request setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public Request setBody(Map<String, String> body) {
        this.body = body;
        return this;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Request setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Request setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
        return this;
    }
}
