package ru.giv13.tasktracker.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.giv13.tasktracker.user.User;

@Component
public interface PrincipalProvider {
    default User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    default Integer getPrincipalId() {
        return getPrincipal().getId();
    }
}
