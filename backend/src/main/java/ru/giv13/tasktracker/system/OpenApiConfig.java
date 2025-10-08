package ru.giv13.tasktracker.system;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Планировщик задач", description = "Backend API", version = "1.0.0", contact = @Contact(name = "Илья Григорьев", email = "giv13@bk.ru", url = "https://vk.com/giv13")))
public class OpenApiConfig {
}
