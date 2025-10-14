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
import ru.giv13.tasktracker.user.UserRepository;

@Service
@RequiredArgsConstructor
public class TaskService implements PrincipalProvider {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Transactional
    public TaskResponseDto create(TaskRequestDto taskRequestDto) {
        Category category = categoryRepository.findByIdAndUserId(taskRequestDto.getCategory(), getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(taskRequestDto.getCategory(), "Category"));
        Task task = mapper.map(taskRequestDto, Task.class);
        task.setIndex(taskRepository.getNextIndexByCategoryId(category.getId()));
        task.setColor(taskRequestDto.getColor() == null ? null : colorRepository.findByIdAndUserId(taskRequestDto.getColor(), getPrincipalId()).orElse(null));
        task.setCategory(category);
        task.setUser(userRepository.getReferenceById(getPrincipalId()));
        return mapper.map(taskRepository.save(task), TaskResponseDto.class);
    }

    @Transactional
    public TaskResponseDto update(Integer id, TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findByIdAndUserId(id, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(id, "Task"));
        mapper.map(taskRequestDto, task);
        task.setColor(taskRequestDto.getColor() == null ? null : colorRepository.findByIdAndUserId(taskRequestDto.getColor(), getPrincipalId()).orElse(null));
        if (!taskRequestDto.getCategory().equals(task.getCategory().getId())) {
            updateCategory(task, taskRequestDto.getCategory());
        }
        taskRepository.save(task);
        return mapper.map(task, TaskResponseDto.class);
    }

    @Transactional
    public void sort(Integer id, TaskSortDto taskSortDto) {
        Task task = taskRepository.findByIdAndUserId(id, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(id, "Task"));
        if (taskSortDto.getCategory() != null) {
            if (taskSortDto.getCategory() == 0) {
                task.setDone(true);
                taskRepository.save(task);
                return;
            }
            task.setDone(false);
            updateCategory(task, taskSortDto.getCategory());
            taskRepository.save(task);
        }
        if (taskSortDto.getOffset() != 0) {
            Integer offsetIndex = taskSortDto.getOffset() > 0
                    ? taskRepository.getMaxOffsetIndex(task.getIndex(), taskSortDto.getOffset(), task.getCategory().getId())
                    : taskRepository.getMinOffsetIndex(task.getIndex(), -taskSortDto.getOffset(), task.getCategory().getId());
            if (offsetIndex != null) {
                if (taskSortDto.getOffset() > 0) {
                    taskRepository.changeIndexes(-1, task.getIndex(), offsetIndex, task.getCategory().getId());
                } else {
                    taskRepository.changeIndexes(1, offsetIndex, task.getIndex(), task.getCategory().getId());
                }
                task.setIndex(offsetIndex);
                taskRepository.save(task);
            }
        }
    }

    @Transactional
    public void delete(Integer id) {
        Task task = taskRepository.findByIdAndUserId(id, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(id, "Task"));
        taskRepository.deleteById(task.getId());
    }

    private void updateCategory(Task task, Integer categoryId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, getPrincipalId()).orElseThrow(() -> new ObjectNotFoundException(categoryId, "Category"));
        Integer offsetIndex = taskRepository.getMaxOffsetIndex(task.getIndex(), Integer.MAX_VALUE, task.getCategory().getId());
        taskRepository.changeIndexes(-1, task.getIndex(), offsetIndex, task.getCategory().getId());
        task.setIndex(taskRepository.getNextIndexByCategoryId(category.getId()));
        task.setCategory(category);
    }
}
