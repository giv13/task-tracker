package ru.giv13.tasktracker.category;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.giv13.tasktracker.color.ColorRepository;
import ru.giv13.tasktracker.user.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final User principal;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryRepository.findAllByUserId(principal.getId(), Sort.by("index"));
    }

    public Category create(CategoryRequestDto categoryRequestDto) {
        Category category = mapper.map(categoryRequestDto, Category.class);
        category.setColor(categoryRequestDto.getColor() == null ? null : colorRepository.findById(categoryRequestDto.getColor()).orElse(null));
        category.setIndex(categoryRepository.getNextIndexByUserId(principal.getId()));
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

    @Transactional
    public void updateOrder(Integer id, OffsetDto offsetDto) {
        Integer offset = offsetDto.getOffset();
        if (offset == 0) return;
        Optional<Category> optCategory = categoryRepository.findByIdAndUserId(id, principal.getId());
        if (optCategory.isPresent()) {
            Category category = optCategory.get();
            Integer offsetIndex = offset > 0
                    ? categoryRepository.getMaxOffsetIndex(category.getIndex(), offset, principal.getId())
                    : categoryRepository.getMinOffsetIndex(category.getIndex(), -offset, principal.getId());
            if (offsetIndex != null) {
                if (offset > 0) {
                    categoryRepository.changeIndexes(-1, category.getIndex(), offsetIndex, principal.getId());
                } else {
                    categoryRepository.changeIndexes(1, offsetIndex, category.getIndex(), principal.getId());
                }
                category.setIndex(offsetIndex);
                categoryRepository.save(category);
            }
        }
    }

    public void delete(Integer id) {
        Category category = categoryRepository.findByIdAndUserId(id, principal.getId()).orElseThrow(() -> new ObjectNotFoundException(id, "Category"));
        categoryRepository.deleteById(category.getId());
    }
}
