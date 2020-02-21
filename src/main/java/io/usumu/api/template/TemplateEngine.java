package io.usumu.api.template;

import java.util.Map;

public interface TemplateEngine {
    String[] getExtensions();

    String render(String file, Map<String, Object> model) throws TemplateProvider.TemplateNotFound;
}
