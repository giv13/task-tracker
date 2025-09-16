package ru.giv13.tasktracker.color;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("colors")
@RequiredArgsConstructor
public class ColorController {
    private final ColorService colorService;

    @GetMapping
    public List<Color> getAll() {
        return colorService.getAll();
    }

    @PostMapping
    public Color create(@Valid @RequestBody ColorRequestDto colorRequestDto) {
        return colorService.create(colorRequestDto);
    }

    @PutMapping("{id}")
    public Color update(@PathVariable("id") Integer id, @Valid @RequestBody ColorRequestDto colorRequestDto) {
        return colorService.update(id, colorRequestDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id) {
        colorService.delete(id);
    }
}
