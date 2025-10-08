package ru.giv13.tasktracker.color;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("colors")
@RequiredArgsConstructor
@Tag(name = "Цвета")
public class ColorController {
    private final ColorService colorService;

    @GetMapping
    @Operation(summary = "Получить список цветов")
    public List<ColorResponseDto> getAll() {
        return colorService.getAll();
    }

    @PostMapping
    @Operation(summary = "Создать цвет")
    public ColorResponseDto create(@Valid @RequestBody ColorRequestDto colorRequestDto) {
        return colorService.create(colorRequestDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Обновить цвет")
    public ColorResponseDto update(
            @PathVariable("id") @Parameter(description = "Идентификатор цвета") Integer id,
            @Valid @RequestBody ColorRequestDto colorRequestDto
    ) {
        return colorService.update(id, colorRequestDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить цвет")
    public void delete(@PathVariable("id") @Parameter(description = "Идентификатор цвета") Integer id) {
        colorService.delete(id);
    }
}
