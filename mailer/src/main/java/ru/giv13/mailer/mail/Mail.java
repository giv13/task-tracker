package ru.giv13.mailer.mail;

import lombok.Getter;

import java.util.Map;

@Getter
public class Mail {
    private final String template;

    private final String to;

    private final String subject;

    private final Map<String, String> model;

    public Mail(String template, String to, String subject, Map<String, String> model) {
        model.put("title", subject);
        this.template = template;
        this.to = to;
        this.subject = subject;
        this.model = model;
    }
}
