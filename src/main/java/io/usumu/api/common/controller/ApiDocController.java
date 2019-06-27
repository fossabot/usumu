package io.usumu.api.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@ApiIgnore
public class ApiDocController {
    @RequestMapping(value = "/")
    public String redirect(HttpServletResponse httpResponse) {
        try {
            httpResponse.sendRedirect("/swagger-ui.html");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}