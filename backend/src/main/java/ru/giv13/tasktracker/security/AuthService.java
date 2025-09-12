package ru.giv13.tasktracker.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.giv13.tasktracker.user.User;
import ru.giv13.tasktracker.user.UserRepository;
import ru.giv13.tasktracker.user.UserRequestDto;
import ru.giv13.tasktracker.user.UserService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    @Value("${security.jwt.refresh.token-name}")
    private String jwtRefreshTokenName;

    public void register(UserRequestDto userRequestDto) {
        User user = modelMapper.map(userRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        auth(user);
    }

    public void login(UserRequestDto userRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword()));
        User user = (User) authentication.getPrincipal();
        auth(user);
    }

    public void refresh(HttpServletRequest request) throws JwtException {
        String refreshToken = jwtService.getCookie(request, jwtRefreshTokenName);
        if (refreshToken != null) {
            String username = jwtService.extractUsername(refreshToken);
            if (username != null) {
                User user = userService.loadUserByUsername(username);
                if (jwtService.isTokenValid(refreshToken, user) && refreshToken.equals(user.getRefresh())) {
                    auth(user);
                    return;
                }
            }
        }
        throw new JwtException("Просроченный или недействительный Refresh-токен");
    }

    public void logout() {
        jwtService.eraseCookie();
    }

    private void auth(User user) {
        user.setRefresh(jwtService.generateCookie(user));
        userRepository.save(user);
    }
}
