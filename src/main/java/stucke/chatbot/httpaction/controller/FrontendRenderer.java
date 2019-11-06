package stucke.chatbot.httpaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendRenderer {

    @GetMapping(path = "/")
    public String renderFrontend() {
        return "index";
    }
}
