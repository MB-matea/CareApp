package hr.aspira.careapp.backend.controller;

import hr.aspira.careapp.backend.model.entities.IndependenceStatus;
import hr.aspira.careapp.backend.model.entities.MobilityStatus;
import hr.aspira.careapp.backend.model.repositories.ResidentRepository;
import hr.aspira.careapp.backend.model.repositories.TaskRepository;
import hr.aspira.careapp.backend.model.repositories.UserRepository;
import org.openapitools.api.UsersApi;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UsersController implements UsersApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public ResponseEntity<List<Object>> usersGet() {
        List<hr.aspira.careapp.backend.model.entities.User> users = userRepository.findAll();
        List<Object> usersReturned = new ArrayList<>();
        List<Object> response = new ArrayList<>();

        for(hr.aspira.careapp.backend.model.entities.User user : users){
            User userReturned = new User();

            userReturned.setUserId(user.getId());
            userReturned.setName(user.getName());
            userReturned.setLastName(user.getLastName());

            usersReturned.add(userReturned);
            response.add(userReturned);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReturnId> usersPost(User user) {
        hr.aspira.careapp.backend.model.entities.User userNew = new hr.aspira.careapp.backend.model.entities.User();

        userNew.setId(user.getUserId());
        userNew.setName(user.getName());
        userNew.setLastName(user.getLastName());
        userNew.setUserName(user.getUserName());
        userNew.setNumber(user.getNumber());
        userNew.setAddress(user.getAddress());
        userNew.setEmail(user.getEmail());
        userNew.setUserName(user.getUserName());
        userNew.setPassword(user.getPassword());
        userNew.setIsAdmin(user.getIsAdmin());

        ReturnId response = new ReturnId();
        response.setId(userNew.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> usersUserIdDelete(Integer userId) {
        Optional<hr.aspira.careapp.backend.model.entities.User> users = userRepository.findById(userId);
        if(users.isPresent()){
            hr.aspira.careapp.backend.model.entities.User user = users.get();
            List<hr.aspira.careapp.backend.model.entities.Task> tasks = user.getTasks();

            taskRepository.deleteAll(tasks);
            userRepository.delete(user);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetSpecificUserResponseBody> usersUserIdGet(Integer userId) {
        Optional<hr.aspira.careapp.backend.model.entities.User> users = userRepository.findById(userId);

        if(!users.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // USER

        hr.aspira.careapp.backend.model.entities.User user = users.get();

        User userReturned = new User();

        userReturned.setUserId(user.getId());
        userReturned.setName(user.getName());
        userReturned.setLastName(user.getLastName());
        userReturned.setAddress(user.getAddress());
        userReturned.setEmail(user.getEmail());
        userReturned.setNumber(user.getNumber());
        userReturned.setIsAdmin(user.getIsAdmin());
        userReturned.setUserName(user.getUserName());
        userReturned.setPassword(user.getPassword());

        // TASKS

        List<hr.aspira.careapp.backend.model.entities.Task> tasks = user.getTasks();
        List<Task> tasksReturned = new ArrayList<>();

        for(hr.aspira.careapp.backend.model.entities.Task task : tasks){
            Task taskReturned = new Task();

            taskReturned.setTaskId(task.getId());
            taskReturned.setDate(task.getDate());
            taskReturned.setUserId(userId);
            taskReturned.setIsDone(task.getIsDone());
            taskReturned.setResidentId(task.getResident().getId());
            taskReturned.setResidentName(task.getResident().getName());
            taskReturned.setResidentLastName(task.getResident().getLastName());

            tasksReturned.add(taskReturned);
        }

        // RESPONSE

        GetSpecificUserResponseBody response = new GetSpecificUserResponseBody();
        response.setUser(userReturned);
        response.setTasks(tasksReturned);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> usersUserIdPut(Integer userId, User user) {
        Optional<hr.aspira.careapp.backend.model.entities.User> usersNew = userRepository.findById(userId);
        hr.aspira.careapp.backend.model.entities.User userNew = usersNew.get();

        userNew.setName(user.getName());
        userNew.setLastName(user.getLastName());
        userNew.setUserName(user.getUserName());
        userNew.setNumber(user.getNumber());
        userNew.setAddress(user.getAddress());
        userNew.setEmail(user.getEmail());
        userNew.setUserName(user.getUserName());
        userNew.setPassword(user.getPassword());
        userNew.setIsAdmin(user.getIsAdmin());

        userRepository.save(userNew);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
