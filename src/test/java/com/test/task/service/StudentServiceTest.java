package com.test.task.service;

import com.test.task.exception.DataNotFoundException;
import com.test.task.model.Group;
import com.test.task.model.Student;
import com.test.task.repository.GroupRepository;
import com.test.task.repository.StudentRepository;
import com.test.task.service.implementations.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("<= StudentService Test =>")
public class StudentServiceTest {

  private final StudentRepository studentRepository;
  private final GroupRepository groupRepository;
  private final StudentService studentService;

  private List<Student> studentsForTest;
  private Group groupForTest;

  @Autowired
  public StudentServiceTest(StudentRepository studentRepository, GroupRepository groupRepository) {
    this.studentRepository = studentRepository;
    this.groupRepository = groupRepository;
    this.studentService = new StudentServiceImpl(studentRepository);
  }

  @BeforeEach
  void setUp() {
    groupForTest = groupRepository.save(initGroup());
    Group anotherGroup = groupRepository.save(initGroup());
    studentsForTest = IntStream
        .range(0, 15)
        .mapToObj(n -> studentRepository.save(initStudent(n < 5 ? groupForTest : anotherGroup)))
        .collect(Collectors.toList());
  }

  @Test
  @DisplayName("<= find Student by id =>")
  void findStudentById() {
    Student studentForTest = studentsForTest.get(0);
    Student student = studentService.findById(studentForTest.getId());
    assertAll(
        () -> assertThat(student).isEqualTo(studentForTest),
        () -> assertThat(student.getName()).isEqualTo(studentForTest.getName()),
        () -> assertThat(student.getSurname()).isEqualTo(studentForTest.getSurname()),
        () -> assertThat(student.getGroup()).isEqualTo(studentForTest.getGroup())
    );
  }

  @Test
  @DisplayName("<= find Student by id throws Exception if doesn't exist =>")
  void findStudentByIdThrowException() {
    Long id = studentsForTest.get(studentsForTest.size() - 1).getId() + 1;
    assertThatThrownBy(() -> studentService.findById(id))
        .isInstanceOf(DataNotFoundException.class)
        .hasMessageContaining(id + "");
  }

  @Test
  @DisplayName("<= find all Student =>")
  void findAllStudent() {
    assertThat(studentService.findAll())
        .isEqualTo(studentsForTest)
        .isEqualTo(studentRepository.findAll());
  }

  @Test
  @DisplayName("<= find all Student when empty =>")
  void findAllStudentWhenEmpty() {
    studentRepository.deleteAll();
    assertThat(studentService.findAll().size())
        .isEqualTo(0);
  }

  @Test
  @DisplayName("<= add new Student =>")
  void addStudent() {
    Student student = initStudent(null);

    Student toTest = studentService.add(student);
    assertAll(
        () -> assertThat(toTest.getId()).isNotNull(),
        () -> assertThat(studentRepository.findAll().size()).isEqualTo(studentsForTest.size() + 1),
        () -> assertThat(studentRepository.findById(toTest.getId())).isNotEmpty()
    );
  }

  @Test
  @DisplayName("<= update Student =>")
  void updateStudent() {
    Student student = studentsForTest.get(0);
    String firstName = student.getName();
    student.setName(firstName + "hello");

    Student toTest = studentService.update(student);
    assertAll(
        () -> assertThat(toTest).isEqualTo(student),
        () -> assertThat(studentRepository.findById(toTest.getId()).orElseThrow().getName())
            .isNotEqualTo(firstName)
    );
  }

  @Test
  @DisplayName("<= delete Student by id =>")
  void deleteStudentById() {
    Long id = studentsForTest.get(0).getId();
    studentService.deleteById(id);

    assertAll(
        () -> assertThat(studentRepository.findAll().size()).isEqualTo(studentsForTest.size() - 1),
        () -> assertThat(studentRepository.findById(id)).isEmpty()
    );
  }

  @Test
  @DisplayName("<= delete Student by id if doesn't exist =>")
  void deleteStudentByIdIfDoesntExist() {
    Long id = studentsForTest.get(0).getId();
    studentService.deleteById(id);

    assertThatThrownBy(() -> studentService.deleteById(id)).hasMessageContaining(id + "");
  }

  @Test
  @DisplayName("<= find all Student by Group id =>")
  void findAllStudentByGroupId(){
    List<Student> toTest = studentsForTest
        .stream()
        .filter(s-> s.getGroup().getId().equals(groupForTest.getId()))
        .collect(Collectors.toList());

    assertThat(studentService.findAllByGroupId(groupForTest.getId())).isEqualTo(toTest);
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
