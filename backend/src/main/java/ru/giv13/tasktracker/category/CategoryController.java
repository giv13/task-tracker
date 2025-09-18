package ru.giv13.tasktracker.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryWithTasksResponseDto> getAll() {
        return categoryService.getAll();
    }

    @PostMapping
    public CategoryResponseDto create(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.create(categoryRequestDto);
    }

    @PutMapping("{id}")
    public CategoryResponseDto update(@PathVariable("id") Integer id, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.update(id, categoryRequestDto);
    }

    @PatchMapping("{id}")
    public void sort(@PathVariable("id") Integer id, @Valid @RequestBody CategorySortDto categorySortDto) {
        categoryService.sort(id, categorySortDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id) {
        categoryService.delete(id);
    }
}
