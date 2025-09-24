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
import ru.giv13.tasktracker.user.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final KafkaTemplate<Integer, UserResponseDto> kafkaTemplate;
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
        return auth(user, userRegisteredTopicName);
    }

    public UserResponseDto login(UserRequestDto userRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword()));
        User user = (User) authentication.getPrincipal();
        return auth(user, userLoggedInTopicName);
    }

    private UserResponseDto auth(User user, String topic) {
        user.setRefresh(jwtService.generateCookie(user));
        UserResponseDto userResponseDto = modelMapper.map(userRepository.save(user), UserResponseDto.class);
        kafkaTemplate.send(topic, user.getId(), userResponseDto);
        return userResponseDto;
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
}
