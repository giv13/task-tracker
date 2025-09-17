package ru.giv13.tasktracker.color;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.giv13.tasktracker.security.PrincipalProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService implements PrincipalProvider {
    private final ColorRepository colorRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<ColorResponseDto> getAll() {
        return colorRepository.findAllByUserId(getPrincipalId()).stream().map(color -> mapper.map(color, ColorResponseDto.class)).toList();
    }

    @Transactional
    public ColorResponseDto create(ColorRequestDto colorRequestDto) {
        Color color = mapper.map(colorRequestDto, Color.class);
        color.setUser(getPrincipal());
        return mapper.map(colorRepository.save(color), ColorResponseDto.class);
    }

    @Transactional
    public ColorResponseDto update(Integer id, ColorRequestDto colorRequestDto) {
        Color color = colorRepository.findByIdAndUserId(id, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(id, "Color"));
        mapper.map(colorRequestDto, color);
        return mapper.map(colorRepository.save(color), ColorResponseDto.class);
    }

    @Transactional
    public void delete(Integer id) {
        Color color = colorRepository.findByIdAndUserId(id, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(id, "Color"));
        colorRepository.deleteById(color.getId());
    }
}
