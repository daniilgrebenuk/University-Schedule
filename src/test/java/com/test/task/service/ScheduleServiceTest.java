package com.test.task.service;

import com.test.task.model.*;
import com.test.task.repository.*;
import com.test.task.service.implementations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("<= ScheduleService Test =>")
public class ScheduleServiceTest {

  private final StudentService studentService;
  private final SubjectService subjectService;
  private final ClassroomService classroomService;
  private final GroupService groupService;
  private final GroupSubjectRepository groupSubjectRepository;
  private final ScheduleService scheduleService;

  private Group groupForTest;
  private Student studentForTest;
  private GroupSubject firstGroupSubject;
  private GroupSubject secondGroupSubject;
  private LocalDate dateForTest;

  @Autowired
  public ScheduleServiceTest(
      StudentRepository studentRepository,
      GroupRepository groupRepository,
      GroupSubjectRepository groupSubjectRepository,
      SubjectRepository subjectRepository,
      ClassroomRepository classroomRepository
  ) {
    this.studentService = new StudentServiceImpl(studentRepository);
    this.subjectService = new SubjectServiceImpl(subjectRepository, groupSubjectRepository);
    this.classroomService = new ClassroomServiceImpl(classroomRepository);
    this.groupService = new GroupServiceImpl(groupRepository, groupSubjectRepository, this.studentService);
    this.groupSubjectRepository = groupSubjectRepository;
    this.scheduleService = new ScheduleServiceImpl(subjectService, classroomService, groupService, groupSubjectRepository);
  }

  @BeforeEach
  void setUp() {
    groupForTest = groupService.add(initGroup());
    studentForTest = studentService.add(initStudent(groupForTest));
    Subject subjectForTest1 = subjectService.add(initSubject());
    Classroom classroomForTest1 = classroomService.add(initClassroom());
    dateForTest = LocalDate.of(
        (int) (Math.random() * 10) + 2021,
        (int) (Math.random() * 10) + 1,
        (int) (Math.random() * 10) + 1
    );
    firstGroupSubject = groupSubjectRepository.save(initGroupSubject(groupForTest, subjectForTest1, classroomForTest1, dateForTest));

    Group groupForTest = groupService.add(initGroup());
    studentService.add(initStudent(groupForTest));
    Subject subjectForTest = subjectService.add(initSubject());
    Classroom classroomForTest = classroomService.add(initClassroom());
    LocalDate date = LocalDate.of(
        (int) (Math.random() * 10) + 2021,
        (int) (Math.random() * 10) + 1,
        (int) (Math.random() * 10) + 1
    );
    secondGroupSubject = groupSubjectRepository.save(initGroupSubject(groupForTest, subjectForTest, classroomForTest, date));
  }

  @Test
  @DisplayName("<= add new Schedule with Group id, Subject id, Classroom id, date =>")
  void addNewSchedule() {
    Group groupForTest = groupService.add(initGroup());
    Subject subjectForTest = subjectService.add(initSubject());
    Classroom classroomForTest = classroomService.add(initClassroom());
    LocalDate date = LocalDate.of(
        (int) (Math.random() * 10) + 2021,
        (int) (Math.random() * 10) + 1,
        (int) (Math.random() * 10) + 1
    );
    GroupSubject groupSubject = initGroupSubject(groupForTest, subjectForTest, classroomForTest, date);
    groupSubject.getId().setIdSubject(groupSubject.getSubject().getId());
    groupSubject.getId().setIdGroup(groupSubject.getGroup().getId());

    GroupSubjectKey key = groupSubject.getId();
    Schedule expected = groupSubject.getSchedule();

    Schedule actual = scheduleService.addSchedule(key, classroomForTest.getId());

    assertAll(
        () -> assertThat(actual.getGroupName()).isEqualTo(expected.getGroupName()),
        () -> assertThat(actual.getSubjectName()).isEqualTo(expected.getSubjectName()),
        () -> assertThat(actual.getClassroomNumber()).isEqualTo(expected.getClassroomNumber()),
        () -> assertThat(actual.getDate()).isEqualTo(expected.getDate())
    );
  }

  @Test
  @DisplayName("<= add new Schedule for multiply week with Group id, Subject id, Classroom id, date, amount od week =>")
  void addScheduleForMultipleWeeks() {
    Group groupForTest = groupService.add(initGroup());
    Subject subjectForTest = subjectService.add(initSubject());
    Classroom classroomForTest = classroomService.add(initClassroom());
    LocalDate date = LocalDate.of(
        (int) (Math.random() * 10) + 2021,
        (int) (Math.random() * 10) + 1,
        (int) (Math.random() * 10) + 1
    );
    GroupSubject groupSubject = initGroupSubject(groupForTest, subjectForTest, classroomForTest, date);
    groupSubject.getId().setIdSubject(groupSubject.getSubject().getId());
    groupSubject.getId().setIdGroup(groupSubject.getGroup().getId());

    GroupSubjectKey key = groupSubject.getId();
    Schedule expected = groupSubject.getSchedule();
    int size = 30;
    List<Schedule> actual = scheduleService.addScheduleForMultipleWeeks(key, classroomForTest.getId(), size);
    AtomicInteger counter = new AtomicInteger();
    assertAll(
        () -> assertThat(actual.size()).isEqualTo(size),
        () -> actual.forEach(s -> assertAll(
            () -> assertThat(s.getGroupName()).isEqualTo(expected.getGroupName()),
            () -> assertThat(s.getSubjectName()).isEqualTo(expected.getSubjectName()),
            () -> assertThat(s.getClassroomNumber()).isEqualTo(expected.getClassroomNumber()),
            () -> assertThat(s.getDate()).isEqualTo(expected.getDate().plusWeeks(counter.getAndIncrement()))
        ))
    );
  }

  @Test
  @DisplayName("<= find all Schedule by Student id and date =>")
  void findAllScheduleByStudentIdAndDate() {
    Schedule expected = firstGroupSubject.getSchedule();
    List<Schedule> actualList = scheduleService.findAllByStudentIdAndDate(studentForTest.getId(), dateForTest);
    Schedule actual = actualList.get(0);
    assertAll(
        () -> assertThat(actualList.size()).isEqualTo(1),
        () -> assertThat(actual.getGroupName()).isEqualTo(expected.getGroupName()),
        () -> assertThat(actual.getSubjectName()).isEqualTo(expected.getSubjectName()),
        () -> assertThat(actual.getClassroomNumber()).isEqualTo(expected.getClassroomNumber()),
        () -> assertThat(actual.getDate()).isEqualTo(expected.getDate())
    );
  }

  @Test
  @DisplayName("<= find all Schedule by Group id and date")
  void findAllScheduleByGroupIdAndDate() {
    Schedule expected = firstGroupSubject.getSchedule();
    List<Schedule> actual = scheduleService.findAllByGroupIdAndDate(groupForTest.getId(), dateForTest);

    assertAll(
        () -> assertThat(actual.size()).isEqualTo(1),
        () -> assertThat(actual.get(0).getGroupName()).isEqualTo(expected.getGroupName()),
        () -> assertThat(actual.get(0).getSubjectName()).isEqualTo(expected.getSubjectName()),
        () -> assertThat(actual.get(0).getClassroomNumber()).isEqualTo(expected.getClassroomNumber()),
        () -> assertThat(actual.get(0).getDate()).isEqualTo(expected.getDate())
    );
  }

  @Test
  @DisplayName("<= update Schedule by GroupSubjectKey =>")
  void updateSchedule(){
    Classroom newClassroom = classroomService.add(initClassroom());

    scheduleService.updateSchedule(firstGroupSubject.getId(), newClassroom.getId());

    assertThat(firstGroupSubject.getClassroom()).isEqualTo(newClassroom);
  }

  @Test
  @DisplayName("<= update Schedule by GroupSubjectKey =>")
  void deleteSchedule(){
    scheduleService.deleteSchedule(firstGroupSubject.getId());

    assertThat(groupSubjectRepository.findAll()).isEqualTo(List.of(secondGroupSubject));
  }


  private GroupSubject initGroupSubject(Group groupForTest, Subject subjectForTest, Classroom classroomForTest, LocalDate data) {
    GroupSubject groupSubject = new GroupSubject();
    groupSubject.getId().setDate(data);
    groupSubject.setGroup(groupForTest);
    groupSubject.setSubject(subjectForTest);
    groupSubject.setClassroom(classroomForTest);

    return groupSubject;
  }

  private Subject initSubject() {
    Subject subject = new Subject();
    subject.setName("" + (int) (Math.random() * 1000));
    return subject;
  }

  private Classroom initClassroom() {
    Classroom classroom = new Classroom();
    classroom.setNumberOfSeats((int) (Math.random() * 3) + 15);
    classroom.setRoomNumber((int) (Math.random() * 400) + 100);
    classroom.setGroupSubjects(new ArrayList<>());
    return classroom;
  }

  private Group initGroup() {
    Group group = new Group();
    group.setSemesterOfStudy((int) (Math.random() * 7) + 1);
    group.setGroupName("" + (int) (Math.random() * 1000));
    return group;
  }

  private Student initStudent(Group group) {
    Student student = new Student();
    student.setName("" + (int) (Math.random() * 1000));
    student.setSurname("" + (int) (Math.random() * 1000));
    student.setGroup(group);
    return student;
  }
}