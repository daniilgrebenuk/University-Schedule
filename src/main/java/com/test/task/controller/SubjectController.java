package com.test.task.controller;

import com.test.task.model.Subject;
import com.test.task.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
@Slf4j
public class SubjectController {

  private final SubjectService subjectService;

  @GetMapping("/get/{id}")
  public ResponseEntity<?> getSubjectById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(subjectService.findById(id));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/all")
  public ResponseEntity<List<Subject>> getAllSubjects() {
    return ResponseEntity.ok(subjectService.findAll());
  }

  @GetMapping("/all/{idGroup}")
  public ResponseEntity<List<Subject>> getAllSubjectsByGroupId(@PathVariable Long idGroup){
    return ResponseEntity.ok(subjectService.findAllByGroupId(idGroup));
  }

  @PostMapping("/add")
  public ResponseEntity<Subject> addSubject(@RequestBody Subject subject) {
    return ResponseEntity.ok(subjectService.add(subject));
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateSubject(@RequestBody Subject subject) {
    try {
      return ResponseEntity.ok(subjectService.update(subject));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteSubjectById(@PathVariable Long id) {
    try {
      subjectService.deleteById(id);
      return ResponseEntity.ok("Successfully deleted!");
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
