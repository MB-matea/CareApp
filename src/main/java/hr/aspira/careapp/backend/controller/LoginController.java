package hr.aspira.careapp.backend.controller;

import hr.aspira.careapp.backend.model.entities.User;
import hr.aspira.careapp.backend.model.repositories.UserRepository;
import org.openapitools.api.LoginApi;
import org.openapitools.model.LoginRequestBody;
import org.openapitools.model.LoginResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController implements LoginApi {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<LoginResponseBody> loginPost(LoginRequestBody loginRequestBody) {
        User user = userRepository.findByUserNameAndPassword(loginRequestBody.getUserName(), loginRequestBody.getPassword());
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            LoginResponseBody response = new LoginResponseBody();
            response.setUserId(user.getId());
            response.setName(user.getName());
            response.setLastName(user.getLastName());
            response.setIsAdmin(user.getIsAdmin());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
