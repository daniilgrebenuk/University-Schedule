package com.test.task.service;

import com.test.task.model.GroupSubjectKey;
import com.test.task.model.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
  Schedule addSchedule(GroupSubjectKey groupSubjectKey, Long idClassroom);
  List<Schedule> addScheduleForMultipleWeeks(GroupSubjectKey groupSubjectKey, Long idClassroom, int amountOfWeek);


  List<Schedule> findAllByStudentIdAndDate(Long idStudent, LocalDate date);
  List<Schedule> findAllByGroupIdAndDate(Long idGroup, LocalDate date);

  Schedule updateSchedule(GroupSubjectKey groupSubjectKey, Long idClassroom);
  void deleteSchedule(GroupSubjectKey groupSubjectKey);

}
