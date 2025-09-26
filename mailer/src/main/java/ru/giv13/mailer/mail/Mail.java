package ru.giv13.mailer.mail;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Mail {
    private final String template;

    private final String to;

    private final String subject;

    private final Map<String, String> model;

    public Mail(String template, String to, String subject, Map<String, String> model) {
        Map<String, String> m = new HashMap<>();
        m.put("title", subject);
        if (model != null && !model.isEmpty()) {
            m.putAll(model);
        }
        this.template = template;
        this.to = to;
        this.subject = subject;
        this.model = m;
    }
}
