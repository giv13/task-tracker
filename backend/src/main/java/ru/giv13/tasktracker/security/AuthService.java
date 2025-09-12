package ru.giv13.tasktracker.security;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.giv13.tasktracker.user.User;
import ru.giv13.tasktracker.user.UserRepository;
import ru.giv13.tasktracker.user.UserRequestDto;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User register(UserRequestDto userRequestDto) {
        User user = modelMapper.map(userRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return auth(user);
    }

    private User auth(User user) {
        user.setRefresh(jwtService.generateCookie(user));
        return userRepository.save(user);
    }
}
