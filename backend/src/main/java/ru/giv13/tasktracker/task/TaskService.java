package ru.giv13.tasktracker.task;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.giv13.tasktracker.category.Category;
import ru.giv13.tasktracker.category.CategoryRepository;
import ru.giv13.tasktracker.color.ColorRepository;
import ru.giv13.tasktracker.security.PrincipalProvider;

@Service
@RequiredArgsConstructor
public class TaskService implements PrincipalProvider {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final ModelMapper mapper;

    @Transactional
    public TaskResponseDto create(TaskRequestDto taskRequestDto) {
        Category category = categoryRepository.findByIdAndUserId(taskRequestDto.getCategory(), getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(taskRequestDto.getCategory(), "Category"));
        Task task = mapper.map(taskRequestDto, Task.class);
        task.setIndex(taskRepository.getNextIndexByUserId(getPrincipalId()));
        task.setColor(taskRequestDto.getColor() == null ? null : colorRepository.findByIdAndUserId(taskRequestDto.getColor(), getPrincipalId()).orElse(null));
        task.setCategory(category);
        task.setUser(getPrincipal());
        return mapper.map(taskRepository.save(task), TaskResponseDto.class);
    }

    @Transactional
    public TaskResponseDto update(Integer id, TaskRequestDto taskRequestDto) {
        Category category = categoryRepository.findByIdAndUserId(taskRequestDto.getCategory(), getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(taskRequestDto.getCategory(), "Category"));
        Task task = taskRepository.findByIdAndUserId(id, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(id, "Task"));
        mapper.map(taskRequestDto, task);
        task.setColor(taskRequestDto.getColor() == null ? null : colorRepository.findByIdAndUserId(taskRequestDto.getColor(), getPrincipalId()).orElse(null));
        task.setCategory(category);
        taskRepository.save(task);
        return mapper.map(task, TaskResponseDto.class);
    }

    @Transactional
    public void delete(Integer id) {
        Task task = taskRepository.findByIdAndUserId(id, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(id, "Task"));
        taskRepository.deleteById(task.getId());
    }
}
