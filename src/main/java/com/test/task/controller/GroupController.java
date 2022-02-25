package com.test.task.controller;

import com.test.task.model.Group;
import com.test.task.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

  private final GroupService groupService;

  @GetMapping("/get/{id}")
  public ResponseEntity<?> getGroupById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(groupService.findById(id));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/all")
  public ResponseEntity<List<Group>> getAllGroups() {
    return ResponseEntity.ok(groupService.findAll());
  }

  @PostMapping("/add")
  public ResponseEntity<Group> addGroup(@RequestBody Group group) {
    return ResponseEntity.ok(groupService.add(group));
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateGroup(@RequestBody Group group) {
    try {
      return ResponseEntity.ok(groupService.update(group));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PatchMapping("/add-student")
  public ResponseEntity<?> addStudentToGroup(@RequestBody Map<String, Long> body){
    try {
      return ResponseEntity.ok(groupService.addStudentToGroup(
          body.get("idStudent"),
          body.get("idGroup")
      ));
    }catch (Exception e){
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteGroupById(@PathVariable Long id) {
    try {
      groupService.deleteById(id);
      return ResponseEntity.ok("Successfully deleted!");
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
