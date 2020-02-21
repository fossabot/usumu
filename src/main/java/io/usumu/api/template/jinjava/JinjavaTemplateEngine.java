package io.usumu.api.template.jinjava;

import com.hubspot.jinjava.Jinjava;
import io.usumu.api.template.TemplateEngine;
import io.usumu.api.template.TemplateProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JinjavaTemplateEngine implements TemplateEngine {
    private final TemplateProvider templateProvider;

    public JinjavaTemplateEngine(final TemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
    }

    @Override
    public String[] getExtensions() {
        return new String[] { "j2" };
    }

    @Override
    public final String render(String file, Map<String, Object> model) throws TemplateProvider.TemplateNotFound {
        Jinjava             jinjava = new Jinjava();

        final String template = templateProvider.load(file);

        jinjava.setResourceLocator((resource, charset, jinjavaInterpreter) -> {
            try {
                return templateProvider.load(resource);
            } catch (TemplateProvider.TemplateNotFound templateNotFound) {
                throw new RuntimeException(templateNotFound);
            }
        });

        return jinjava.render(template, model);
    }
}
