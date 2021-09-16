package hr.aspira.careapp.backend.controller;

import hr.aspira.careapp.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<String> testData() {

        testService.fillTestData();

        return new ResponseEntity<>("Data input successful", HttpStatus.OK);
    }
}
