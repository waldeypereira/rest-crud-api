package com.wfrocha.demo.rest;

import com.wfrocha.demo.entity.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/api")
public class StudentRestController {

    private List<Student> theStudents;

    @PostConstruct
    public void loadData() {
        theStudents = new ArrayList<>();
        theStudents.add(new Student("Fran", "Rocha"));
        theStudents.add(new Student("Fabricio", "Peralta"));
        theStudents.add(new Student("Diego", "Pereira"));
    }

    @GetMapping("/students")
    public List<Student> getStudents() {
        return theStudents;
    }

    @GetMapping("students/{studentId}")
    public Student getStudent(@PathVariable int studentId) {

        if (studentId >= theStudents.size() || studentId < 0) {
            throw new StudentNotFoundException("Student id not found - " + studentId);
        }

        return theStudents.get(studentId);
    }

    // add an exception handler using @ExceptionHandler

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException ex) {
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage("Student id not found");
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, org.springframework.http.HttpStatus.NOT_FOUND);
    }

    // add another exception handler ... to catch any exception (catch all)
    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(Exception ex) {
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Student id not found");
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
