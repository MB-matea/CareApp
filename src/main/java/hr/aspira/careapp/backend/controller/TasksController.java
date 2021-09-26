package hr.aspira.careapp.backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hr.aspira.careapp.backend.common.LocalDateDeserializer;
import hr.aspira.careapp.backend.model.entities.Resident;
import hr.aspira.careapp.backend.model.entities.User;
import hr.aspira.careapp.backend.model.repositories.ResidentRepository;
import hr.aspira.careapp.backend.model.repositories.TaskRepository;
import hr.aspira.careapp.backend.model.repositories.UserRepository;
import org.openapitools.api.TasksApi;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TasksController implements TasksApi {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResidentRepository residentRepository;

    private final ObjectMapper objectMapper;

    public TasksController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        this.objectMapper.registerModule(module);
    }

    @Override
    public ResponseEntity<ReturnId> tasksPost(CreateNewTaskRequestBody createNewTaskRequestBody) {
        hr.aspira.careapp.backend.model.entities.Task taskNew = new hr.aspira.careapp.backend.model.entities.Task();
        Optional<User> user = userRepository.findById(createNewTaskRequestBody.getUserId());
        Optional<Resident> resident = residentRepository.findById(createNewTaskRequestBody.getResidentId());

        if(user.isEmpty() || resident.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        taskNew.setUser(user.get());
        taskNew.setResident(resident.get());
        taskNew.setDate(createNewTaskRequestBody.getDate());

        taskRepository.save(taskNew);


        ReturnId response = new ReturnId();
        response.setId(taskNew.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> tasksTaskIdDelete(Integer taskId) {
        Optional<hr.aspira.careapp.backend.model.entities.Task> task = taskRepository.findById(taskId);
        if(task.isPresent()){
            taskRepository.delete(task.get());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> tasksTaskIdPut(Integer taskId, Task task) {
        Optional<hr.aspira.careapp.backend.model.entities.Task> tasksNew = taskRepository.findById(taskId);
        if(!tasksNew.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        hr.aspira.careapp.backend.model.entities.Task taskNew = tasksNew.get();

        Optional<Resident> resident = residentRepository.findById(task.getResidentId());
        Optional<User> user = userRepository.findById(task.getUserId());
        if(resident.isEmpty() || user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        taskNew.setIsDone(task.getIsDone());
        taskNew.setDate(task.getDate());
        taskNew.setResident(resident.get());
        taskNew.setUser(user.get());

        taskRepository.save(taskNew);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetAllTasksForSpecificUserResponseBody> tasksUserIdGet(Integer userId, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDate date) {
        Optional<hr.aspira.careapp.backend.model.entities.User> users = userRepository.findById(userId);

        if(!users.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        hr.aspira.careapp.backend.model.entities.User user = users.get();
        List<hr.aspira.careapp.backend.model.entities.Task> tasks = user.getTasks();
        List<Task> tasksReturned = new ArrayList<>();

        for (hr.aspira.careapp.backend.model.entities.Task task : tasks){
            Task taskReturned =  new Task();
            if(task.getDate().isEqual(date)){
                taskReturned.setTaskId(task.getId());
                taskReturned.setResidentId(task.getResident().getId());
                taskReturned.setResidentName(task.getResident().getName());
                taskReturned.setResidentLastName(task.getResident().getLastName());
                taskReturned.setUserId(task.getUser().getId());
                taskReturned.setDate(task.getDate());
                taskReturned.setIsDone(task.getIsDone());

                tasksReturned.add(taskReturned);
            }
        }

        // RESPONSE
        GetAllTasksForSpecificUserResponseBody response = new GetAllTasksForSpecificUserResponseBody();
        response.setTasks(tasksReturned);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @Override
    public ResponseEntity<GetAllTasksForSpecificResidentResponseBody> tasksResidentResidentIdGet(Integer residentId) {
        Optional<hr.aspira.careapp.backend.model.entities.Resident> residents = residentRepository.findById(residentId);

        if(!residents.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        hr.aspira.careapp.backend.model.entities.Resident resident = residents.get();
        List<hr.aspira.careapp.backend.model.entities.Task> tasks = resident.getTasks();
        List<Task> tasksReturned = new ArrayList<>();

        for (hr.aspira.careapp.backend.model.entities.Task task : tasks){
            Task taskReturned =  new Task();

            taskReturned.setTaskId(task.getId());
            taskReturned.setResidentId(task.getResident().getId());
            taskReturned.setResidentName(task.getResident().getName());
            taskReturned.setResidentLastName(task.getResident().getLastName());
            taskReturned.setUserId(task.getUser().getId());
            taskReturned.setUserName(task.getUser().getName());
            taskReturned.setUserLastName(task.getUser().getLastName());
            taskReturned.setDate(task.getDate());
            taskReturned.setIsDone(task.getIsDone());

            tasksReturned.add(taskReturned);
        }

        // RESPONSE
        GetAllTasksForSpecificResidentResponseBody response = new GetAllTasksForSpecificResidentResponseBody();
        response.setTasks(tasksReturned);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
