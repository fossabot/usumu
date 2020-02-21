package io.usumu.api.template;

public interface TemplateProvider {
    String load(String template) throws TemplateNotFound;

    void save(String file, String content);

    class TemplateNotFound extends Exception {
        public final String template;

        public TemplateNotFound(final String template) {
            super("Template not found: " + template);
            this.template = template;
        }
    }
}
