package ru.giv13.tasktracker.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.giv13.tasktracker.security.PrincipalProvider;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, PrincipalProvider {
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Пользователь " + email + " не найден"));
    }

    @Transactional
    public UserResponseDto update(UserProfileDto userProfileDto) {
        userProfileDto.encodePassword(passwordEncoder);
        mapper.map(userProfileDto, getPrincipal());
        return mapper.map(userRepository.save(getPrincipal()), UserResponseDto.class);
    }
}
