package ru.giv13.tasktracker.category;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.giv13.tasktracker.color.ColorRepository;
import ru.giv13.tasktracker.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final User principal;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryRepository.findAllByUserId(principal.getId());
    }

    public Category create(CategoryRequestDto categoryRequestDto) {
        Category category = mapper.map(categoryRequestDto, Category.class);
        category.setColor(categoryRequestDto.getColor() == null ? null : colorRepository.findById(categoryRequestDto.getColor()).orElse(null));
        User user = new User();
        user.setId(principal.getId());
        category.setUser(user);
        return categoryRepository.save(category);
    }

    public Category update(Integer id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findByIdAndUserId(id, principal.getId()).orElseThrow(() -> new ObjectNotFoundException(id, "Category"));
        mapper.map(categoryRequestDto, category);
        category.setColor(categoryRequestDto.getColor() == null ? null : colorRepository.findById(categoryRequestDto.getColor()).orElse(null));
        categoryRepository.save(category);
        return category;
    }

    public void delete(Integer id) {
        Category category = categoryRepository.findByIdAndUserId(id, principal.getId()).orElseThrow(() -> new ObjectNotFoundException(id, "Category"));
        categoryRepository.deleteById(category.getId());
    }
}
