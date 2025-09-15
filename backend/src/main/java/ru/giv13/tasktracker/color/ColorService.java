package ru.giv13.tasktracker.color;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.giv13.tasktracker.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;
    private final User principal;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<Color> getAll() {
        return colorRepository.findAllByUserId(principal.getId());
    }

    public Color create(ColorRequestDto colorRequestDto) {
        Color color = mapper.map(colorRequestDto, Color.class);
        User user = new User();
        user.setId(principal.getId());
        color.setUser(user);
        return colorRepository.save(color);
    }

    public Color update(Integer id, ColorRequestDto colorRequestDto) {
        Color color = colorRepository.findByIdAndUserId(id, principal.getId()).orElseThrow(() -> new ObjectNotFoundException(id, "Color"));
        mapper.map(colorRequestDto, color);
        return colorRepository.save(color);
    }

    public Color delete(Integer id) {
        Color color = colorRepository.findByIdAndUserId(id, principal.getId()).orElseThrow(() -> new ObjectNotFoundException(id, "Color"));
        colorRepository.deleteById(color.getId());
        return color;
    }
}
