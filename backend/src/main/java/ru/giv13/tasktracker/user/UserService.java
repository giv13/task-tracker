package ru.giv13.tasktracker.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.giv13.common.event.UserPasswordChangedEvent;
import ru.giv13.tasktracker.security.PrincipalProvider;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, PrincipalProvider {
    private final ModelMapper mapper;
    private final KafkaTemplate<Integer, UserPasswordChangedEvent> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${encryptor.password}")
    private String encryptorPassword;

    @Value("${encryptor.salt}")
    private String encryptorSalt;

    @Value("${spring.kafka.topics.user.password-changed}")
    private String userPasswordChangedTopicName;

    @Transactional(readOnly = true)
    public User loadUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Пользователь с ID=" + id + " не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Пользователь " + email + " не найден"));
    }

    @Transactional
    public UserResponseDto update(UserProfileDto userProfileDto) {
        User user = loadUserById(getPrincipalId());
        mapper.map(userProfileDto, user);
        if (userProfileDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);

        if (userProfileDto.getPassword() != null) {
            UserPasswordChangedEvent userPasswordChangedEvent = new UserPasswordChangedEvent()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .setPassword(Encryptors.delux(encryptorPassword, encryptorSalt).encrypt(userProfileDto.getPassword()));
            kafkaTemplate.send(userPasswordChangedTopicName, user.getId(), userPasswordChangedEvent);
        }

        return mapper.map(user, UserResponseDto.class);
    }
}
