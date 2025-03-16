//package com.task_management_system.task_management_system.service.impl;
//
//import com.task_management_system.task_management_system.model.Task;
//import com.task_management_system.task_management_system.model.dto.TaskCreateDTO;
//import com.task_management_system.task_management_system.model.dto.TaskDTO;
//import com.task_management_system.task_management_system.repository.TaskRepository;
//import com.task_management_system.task_management_system.service.TaskService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TaskServiceImpl implements TaskService {
//
//    public ModelMapper mapper = new ModelMapper();
//
//    private final TaskRepository taskRepository;
//
//    @Autowired
//    public TaskServiceImpl(TaskRepository taskRepository) {
//        this.taskRepository = taskRepository;
//    }
//
//    @Override
//    public TaskDTO createTask(TaskCreateDTO taskCreate) {
//        Task taskSave = mapper.map(taskCreate, Task.class);
//        Task taskSaveReturn = taskRepository.save(taskSave);
//        TaskDTO taskCreated = mapper.map(taskSaveReturn, TaskDTO.class);
//        return taskCreated;
//    }
//
//    @Override
//    public TaskDTO updateTask(Long taskId, TaskDTO task) {
//        Task getTaskFromDB = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Not found"));
//        Task updateTask = mapper.map(task, Task.class);
//
////        getTaskFromDB.setTaskPriority(updateTask.getTaskPriority());
////        getTaskFromDB.setTaskStatus(updateTask.getTaskStatus());
////        getTaskFromDB.setAssignee(updateTask.getAssignee());
////        getTaskFromDB.setDescription(updateTask.getDescription());
////        getTaskFromDB.setTitle(updateTask.getTitle());
//
//        Task saveTask = taskRepository.save(getTaskFromDB);
//
//        return mapper.map(saveTask, TaskDTO.class);
//    }
//
//    @Override
//    public void deleteTask(Long taskId) {
//        taskRepository.deleteById(taskId);
//    }
//
//    @Override
//    public TaskDTO getTaskById(Long taskId) {
//        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Not found"));
//        TaskDTO taskDTO = mapper.map(task, TaskDTO.class);
//        return taskDTO;
//    }
//}
