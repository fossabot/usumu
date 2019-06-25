package io.usumu.api.common.controller;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.web.Swagger2Controller;

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