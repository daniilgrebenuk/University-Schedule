package com.test.task.controller;

import com.test.task.model.Classroom;
import com.test.task.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
@Slf4j
public class ClassroomController {
  private final ClassroomService classroomService;

  @GetMapping("/get/{id}")
  public ResponseEntity<?> getClassroomById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(classroomService.findById(id));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/all")
  public ResponseEntity<List<Classroom>> getAllClassrooms() {
    return ResponseEntity.ok(classroomService.findAll());
  }


  @PostMapping("/add")
  public ResponseEntity<Classroom> addClassroom(@RequestBody Classroom classroom) {
    return ResponseEntity.ok(classroomService.add(classroom));
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateClassroom(@RequestBody Classroom classroom) {
    try {
      return ResponseEntity.ok(classroomService.update(classroom));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteClassroomById(@PathVariable Long id) {
    try {
      classroomService.deleteById(id);
      return ResponseEntity.ok("Successfully deleted!");
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
