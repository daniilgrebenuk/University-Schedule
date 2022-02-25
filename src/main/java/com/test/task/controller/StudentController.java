package com.test.task.controller;

import com.test.task.model.Student;
import com.test.task.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

  private final StudentService studentService;

  @GetMapping("/get/{id}")
  public ResponseEntity<?> getStudentById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(studentService.findById(id));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/all")
  public ResponseEntity<List<Student>> getAllStudents() {
    return ResponseEntity.ok(studentService.findAll());
  }

  @GetMapping("/all/{idGroup}")
  public ResponseEntity<List<Student>> getAllStudentsByGroupId(@PathVariable Long idGroup){
    return ResponseEntity.ok(studentService.findAllByGroupId(idGroup));
  }

  @PostMapping("/add")
  public ResponseEntity<Student> addStudent(@RequestBody Student student) {
    return ResponseEntity.ok(studentService.add(student));
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateStudent(@RequestBody Student student) {
    try {
      return ResponseEntity.ok(studentService.update(student));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteStudentById(@PathVariable Long id) {
    try {
      studentService.deleteById(id);
      return ResponseEntity.ok("Successfully deleted!");
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
