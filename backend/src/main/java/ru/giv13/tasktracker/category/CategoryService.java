package ru.giv13.tasktracker.category;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.giv13.tasktracker.color.ColorRepository;
import ru.giv13.tasktracker.security.PrincipalProvider;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements PrincipalProvider {
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<CategoryWithTasksResponseDto> getAll() {
        return categoryRepository.findAllByUserId(getPrincipalId(), Sort.by("index")).stream().map(category -> mapper.map(category, CategoryWithTasksResponseDto.class)).toList();
    }

    @Transactional
    public CategoryResponseDto create(CategoryRequestDto categoryRequestDto) {
        Category category = mapper.map(categoryRequestDto, Category.class);
        category.setIndex(categoryRepository.getNextIndexByUserId(getPrincipalId()));
        category.setColor(categoryRequestDto.getColor() == null ? null : colorRepository.findByIdAndUserId(categoryRequestDto.getColor(), getPrincipalId()).orElse(null));
        category.setUser(getPrincipal());
        return mapper.map(categoryRepository.save(category), CategoryResponseDto.class);
    }

    @Transactional
    public CategoryResponseDto update(Integer id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findByIdAndUserId(id, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(id, "Category"));
        mapper.map(categoryRequestDto, category);
        category.setColor(categoryRequestDto.getColor() == null ? null : colorRepository.findByIdAndUserId(categoryRequestDto.getColor(), getPrincipalId()).orElse(null));
        categoryRepository.save(category);
        return mapper.map(category, CategoryResponseDto.class);
    }

    @Transactional
    public void updateOrder(Integer id, OffsetDto offsetDto) {
        Integer offset = offsetDto.getOffset();
        if (offset == 0) return;
        Optional<Category> optCategory = categoryRepository.findByIdAndUserId(id, getPrincipalId());
        if (optCategory.isPresent()) {
            Category category = optCategory.get();
            Integer offsetIndex = offset > 0
                    ? categoryRepository.getMaxOffsetIndex(category.getIndex(), offset, getPrincipalId())
                    : categoryRepository.getMinOffsetIndex(category.getIndex(), -offset, getPrincipalId());
            if (offsetIndex != null) {
                if (offset > 0) {
                    categoryRepository.changeIndexes(-1, category.getIndex(), offsetIndex, getPrincipalId());
                } else {
                    categoryRepository.changeIndexes(1, offsetIndex, category.getIndex(), getPrincipalId());
                }
                category.setIndex(offsetIndex);
                categoryRepository.save(category);
            }
        }
    }

    @Transactional
    public void delete(Integer id) {
        Category category = categoryRepository.findByIdAndUserId(id, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(id, "Category"));
        categoryRepository.deleteById(category.getId());
    }
}
