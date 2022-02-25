package com.test.task.service.implementations;

import com.test.task.exception.DataNotFoundException;
import com.test.task.model.*;
import com.test.task.repository.GroupSubjectRepository;
import com.test.task.service.ClassroomService;
import com.test.task.service.GroupService;
import com.test.task.service.ScheduleService;
import com.test.task.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final SubjectService subjectService;
  private final ClassroomService classroomService;
  private final GroupService groupService;
  private final GroupSubjectRepository groupSubjectRepository;


  @Override
  public Schedule addSchedule(GroupSubjectKey groupSubjectKey, Long idClassroom) {
    GroupSubject groupSubject = createGroupSubject(groupSubjectKey, idClassroom);

    return groupSubjectRepository.save(groupSubject).getSchedule();
  }

  @Override
  public List<Schedule> addScheduleForMultipleWeeks(GroupSubjectKey groupSubjectKey, Long idClassroom, int amountOfWeek) {
    List<GroupSubjectKey> keys = new ArrayList<>();

    GroupSubject groupSubject = createGroupSubject(groupSubjectKey, idClassroom);

    groupSubject = groupSubjectRepository.save(groupSubject);

    keys.add(new GroupSubjectKey(groupSubject.getId()));

    LocalDate localDate = keys.get(0).getDate();

    for (int i = 1; i < amountOfWeek; i++) {
      localDate = localDate.plusWeeks(1);

      GroupSubjectKey key = new GroupSubjectKey(groupSubject.getId());
      key.setDate(localDate);
      keys.add(key);
      groupSubject = createGroupSubject(key, idClassroom);
      groupSubjectRepository.save(groupSubject);
    }

    return keys
        .stream()
        .map(k -> groupSubjectRepository.findById(k).orElseThrow().getSchedule())
        .collect(Collectors.toList());
  }

  @Override
  public List<Schedule> findAllByStudentIdAndDate(Long idStudent, LocalDate date) {
    return groupSubjectRepository.findAllScheduleByStudentIdAndDate(idStudent, date);
  }

  @Override
  public List<Schedule> findAllByGroupIdAndDate(Long idGroup, LocalDate date) {
    return groupSubjectRepository.findAllScheduleByGroupIdAndDate(idGroup, date);
  }

  @Override
  public Schedule updateSchedule(GroupSubjectKey groupSubjectKey, Long idClassroom) {
    GroupSubject groupSubject = groupSubjectRepository
        .findById(groupSubjectKey)
        .orElseThrow(() -> new DataNotFoundException("Schedule with id: \"" + groupSubjectKey + "\" doesn't exist!"));
    Classroom classroom = classroomService.findById(idClassroom);
    groupSubject.setClassroom(classroom);


    return groupSubjectRepository.save(groupSubject).getSchedule();
  }

  @Override
  public void deleteSchedule(GroupSubjectKey groupSubjectKey) {
    groupSubjectRepository.deleteById(groupSubjectKey);
  }

  private GroupSubject createGroupSubject(GroupSubjectKey groupSubjectKey, Long idClassroom) {
    Group group = groupService.findById(groupSubjectKey.getIdGroup());
    Subject subject = subjectService.findById(groupSubjectKey.getIdSubject());


    GroupSubject groupSubject = new GroupSubject();
    groupSubject.setId(groupSubjectKey);
    groupSubject.setGroup(group);
    groupSubject.setSubject(subject);

    if (idClassroom != null) {
      Classroom classroom = classroomService.findById(idClassroom);
      groupSubject.setClassroom(classroom);
    }
    return groupSubject;
  }
}
