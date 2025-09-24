package ru.giv13.tasktracker.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.giv13.common.event.UserLoggedInEvent;
import ru.giv13.common.event.UserRegisteredEvent;
import ru.giv13.tasktracker.user.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final KafkaTemplate<Integer, UserRegisteredEvent> kafkaUserRegisteredTemplate;
    private final KafkaTemplate<Integer, UserLoggedInEvent> kafkaUserLoggedInTemplate;
    private final JwtService jwtService;
    private final UserService userService;

    @Value("${spring.kafka.topics.user.registered}")
    private String userRegisteredTopicName;

    @Value("${spring.kafka.topics.user.logged-in}")
    private String userLoggedInTopicName;

    @Value("${security.jwt.refresh.token-name}")
    private String jwtRefreshTokenName;

    public UserResponseDto register(UserRequestDto userRequestDto) {
        User user = modelMapper.map(userRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserResponseDto userResponseDto = auth(user);

        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent()
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setPassword(userRequestDto.getPassword());
        kafkaUserRegisteredTemplate.send(userRegisteredTopicName, user.getId(), userRegisteredEvent);

        return userResponseDto;
    }

    public UserResponseDto login(UserRequestDto userRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword()));
        User user = (User) authentication.getPrincipal();
        UserResponseDto userResponseDto = auth(user);

        UserLoggedInEvent userLoggedInEvent = new UserLoggedInEvent()
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setIp(getClientIp());
        kafkaUserLoggedInTemplate.send(userLoggedInTopicName, user.getId(), userLoggedInEvent);

        return userResponseDto;
    }

    private UserResponseDto auth(User user) {
        user.setRefresh(jwtService.generateCookie(user));
        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
    }

    public void refresh(HttpServletRequest request) throws JwtException {
        String refreshToken = jwtService.getCookie(request, jwtRefreshTokenName);
        if (refreshToken != null) {
            String username = jwtService.extractUsername(refreshToken);
            if (username != null) {
                User user = userService.loadUserByUsername(username);
                if (jwtService.isTokenValid(refreshToken, user) && refreshToken.equals(user.getRefresh())) {
                    user.setRefresh(jwtService.generateCookie(user));
                    userRepository.save(user);
                    return;
                }
            }
        }
        throw new JwtException("Просроченный или недействительный Refresh-токен");
    }

    public void logout() {
        jwtService.eraseCookie();
    }

    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }
}
