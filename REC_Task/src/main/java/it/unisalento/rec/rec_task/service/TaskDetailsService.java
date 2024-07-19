package it.unisalento.rec.rec_task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_task.domain.Task;
import it.unisalento.rec.rec_task.dto.SendTaskDTO;
import it.unisalento.rec.rec_task.dto.TaskBillDTO;
import it.unisalento.rec.rec_task.dto.TaskDTO;
import it.unisalento.rec.rec_task.dto.TaskListDTO;
import it.unisalento.rec.rec_task.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_task.exceptions.TaskNotFoundException;
import it.unisalento.rec.rec_task.repositories.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskDetailsService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    private RabbitMQService rabbitMQService;


    public TaskDTO createTask(TaskDTO taskDTO) throws JsonProcessingException, OperationNotPermittedException {
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task, "id");
        SendTaskDTO sendTaskDTO = new SendTaskDTO();
        BeanUtils.copyProperties(taskDTO, sendTaskDTO);

        String response = rabbitMQService.sendTask(sendTaskDTO);

        if(!response.equals("OK")) {
            throw new OperationNotPermittedException("Insufficient credit to submit the task");
        }

        task = taskRepository.save(task);
        taskDTO.setId(task.getId());

        TaskBillDTO taskBillDTO = new TaskBillDTO();
        BeanUtils.copyProperties(task, taskBillDTO, "cost");
        taskBillDTO.setCost(task.getExecutionTime() * 2);

        rabbitMQService.sendBillTask(taskBillDTO);

        return taskDTO;
    }

    public TaskListDTO searchTaskByClientEmail(String clientEmail) throws TaskNotFoundException{
        TaskListDTO taskListDTO = new TaskListDTO();
        ArrayList<TaskDTO> tasks = new ArrayList<>();
        taskListDTO.setList(tasks);

        List<Task> list = taskRepository.findByClientEmail(clientEmail);

        if(list.isEmpty()){
           throw new TaskNotFoundException("No submissive task by " + clientEmail);
        }

        for(Task task : list) {
            TaskDTO taskDTO = new TaskDTO();
            BeanUtils.copyProperties(task, taskDTO);
            tasks.add(taskDTO);
        }
        return taskListDTO;
    }

    public TaskDTO searchById(String id) throws TaskNotFoundException {
        TaskDTO taskDTO = new TaskDTO();
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundException("Non-existing task");
        }

        Task task = optionalTask.get();
        BeanUtils.copyProperties(task, taskDTO);
        return taskDTO;
    }

    public String deleteTask(String id)throws TaskNotFoundException{
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("The task does not exist");
        }
        taskRepository.deleteById(id);
        return id;
    }

    public TaskDTO updateTask(String id, TaskDTO taskDTO) throws TaskNotFoundException{
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundException("the task does not exist");
        }
            Task task = optionalTask.get();
            BeanUtils.copyProperties(taskDTO, task, "id");
            task = taskRepository.save(task);
            taskDTO.setId(task.getId());
            return taskDTO;
    }
}
