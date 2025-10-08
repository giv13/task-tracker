package ru.giv13.tasktracker.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
@Tag(name = "Категории")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Получить список категорий с задачами")
    public List<CategoryWithTasksResponseDto> getAll() {
        return categoryService.getAll();
    }

    @PostMapping
    @Operation(summary = "Создать категорию")
    public CategoryResponseDto create(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.create(categoryRequestDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Обновить категорию")
    public CategoryResponseDto update(
            @PathVariable("id") @Parameter(description = "Идентификатор категории") Integer id,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        return categoryService.update(id, categoryRequestDto);
    }

    @PatchMapping("{id}")
    @Operation(summary = "Сортировать категорию", description = "Изменение порядка (индекса) категории")
    public void sort(
            @PathVariable("id") @Parameter(description = "Идентификатор категории") Integer id,
            @Valid @RequestBody CategorySortDto categorySortDto
    ) {
        categoryService.sort(id, categorySortDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить категорию")
    public void delete(@PathVariable("id") @Parameter(description = "Идентификатор категории") Integer id) {
        categoryService.delete(id);
    }
}
