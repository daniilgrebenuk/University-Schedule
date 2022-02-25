package com.test.task.controller;

import com.test.task.model.GroupSubjectKey;
import com.test.task.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

  private final ScheduleService scheduleService;

  @PostMapping("/add")
  public ResponseEntity<?> addNewSchedule(@RequestBody Map<String, String> body){
    GroupSubjectKey groupSubjectKey;
    Long idClassroom = null;

    try {
      groupSubjectKey = mapToGroupSubjectKey(body);

      if (body.containsKey("idClassroom")){
        idClassroom = Long.parseLong(body.get("idClassroom"));
      }
    }catch (Exception e){
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    return ResponseEntity.ok(scheduleService.addSchedule(groupSubjectKey, idClassroom));
  }

  @PostMapping("/add-multiply")
  public ResponseEntity<?> addNewScheduleForMultiplyWeeks(@RequestBody Map<String, String> body){
    GroupSubjectKey groupSubjectKey;
    Long idClassroom = null;
    int amountOfWeeks;

    try {
      groupSubjectKey = mapToGroupSubjectKey(body);

      amountOfWeeks = Integer.parseInt(body.get("amountOfWeeks"));
      if (body.containsKey("idClassroom")){
        idClassroom = Long.parseLong(body.get("idClassroom"));
      }
    }catch (Exception e){
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    return ResponseEntity.ok(scheduleService.addScheduleForMultipleWeeks(groupSubjectKey, idClassroom, amountOfWeeks));
  }

  @GetMapping("/student")
  public ResponseEntity<?> getScheduleByStudentIdAndDate(@RequestBody Map<String, String> body){
    long idStudent;
    LocalDate date;
    try {
      idStudent = Long.parseLong(body.get("idStudent"));

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      date = LocalDate.parse(body.get("date"),formatter);
    }catch (Exception e){
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    return ResponseEntity.ok(scheduleService.findAllByStudentIdAndDate(idStudent, date));
  }

  @GetMapping("/group")
  public ResponseEntity<?> getScheduleByGroupIdAndDate(@RequestBody Map<String, String> body){
    long idGroup;
    LocalDate date;
    try {
      idGroup = Long.parseLong(body.get("idGroup"));

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      date = LocalDate.parse(body.get("date"),formatter);
    }catch (Exception e){
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    return ResponseEntity.ok(scheduleService.findAllByGroupIdAndDate(idGroup, date));
  }

  @PatchMapping("/update")
  public ResponseEntity<?> updateSchedule(@RequestBody Map<String, String> body){
    GroupSubjectKey groupSubjectKey;
    Long idClassroom = null;

    try {
      groupSubjectKey = mapToGroupSubjectKey(body);

      if (body.containsKey("idClassroom")){
        idClassroom = Long.parseLong(body.get("idClassroom"));
      }
    }catch (Exception e){
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    return ResponseEntity.ok(scheduleService.updateSchedule(groupSubjectKey, idClassroom));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteSchedule(@RequestBody Map<String, String> body){
    try {
      GroupSubjectKey key = mapToGroupSubjectKey(body);
      scheduleService.deleteSchedule(key);
      return ResponseEntity.ok("Successfully deleted!");
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  private GroupSubjectKey mapToGroupSubjectKey(Map<String, String> body){
    Long idGroup = Long.parseLong(body.get("idGroup"));
    Long idSubject = Long.parseLong(body.get("idSubject"));

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate date = LocalDate.parse(body.get("date"),formatter);
    return new GroupSubjectKey(idGroup, idSubject, date);
  }
}
