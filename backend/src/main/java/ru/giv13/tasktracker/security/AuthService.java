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
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.giv13.common.event.UserRegisteredEvent;
import ru.giv13.tasktracker.user.*;

import java.security.MessageDigest;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final KafkaTemplate<Integer, UserRegisteredEvent> kafkaTemplate;
    private final JwtService jwtService;
    private final UserService userService;

    @Value("${encryptor.password}")
    private String encryptorPassword;

    @Value("${encryptor.salt}")
    private String encryptorSalt;

    @Value("${spring.kafka.topics.user.registered}")
    private String userRegisteredTopicName;

    @Value("${security.jwt.refresh.token-name}")
    private String jwtRefreshTokenName;

    public UserResponseDto register(UserRequestDto userRequestDto) {
        User user = modelMapper.map(userRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserResponseDto userResponseDto = auth(user);

        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent()
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setPassword(Encryptors.delux(encryptorPassword, encryptorSalt).encrypt(userRequestDto.getPassword()));
        kafkaTemplate.send(userRegisteredTopicName, user.getId(), userRegisteredEvent);

        return userResponseDto;
    }

    public UserResponseDto login(UserRequestDto userRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword()));
        User user = (User) authentication.getPrincipal();
        return auth(user);
    }

    private UserResponseDto auth(User user) {
        setRefresh(user);
        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
    }

    public void refresh(HttpServletRequest request) throws JwtException {
        String refreshToken = jwtService.getCookie(request, jwtRefreshTokenName);
        if (refreshToken != null && jwtService.isTokenValid(refreshToken)) {
            Integer userId = jwtService.extractUserId(refreshToken);
            if (userId != null) {
                User user = userService.loadUserById(userId);
                if (user.getRefresh() != null && passwordEncoder.matches(getHash(refreshToken), user.getRefresh())) {
                    setRefresh(user);
                    userRepository.save(user);
                    return;
                }
            }
        } else {
            jwtService.eraseCookie();
        }
        throw new JwtException("Просроченный или недействительный Refresh-токен");
    }

    private void setRefresh(User user) {
        String refreshToken = jwtService.generateCookie(user);
        String hashedRefreshToken = getHash(refreshToken);
        user.setRefresh(passwordEncoder.encode(hashedRefreshToken));
    }

    private String getHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        jwtService.eraseCookie();
    }
}
