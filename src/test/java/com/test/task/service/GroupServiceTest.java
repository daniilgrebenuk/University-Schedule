package com.test.task.service;

import com.test.task.exception.DataNotFoundException;
import com.test.task.model.Group;
import com.test.task.model.Student;
import com.test.task.repository.GroupRepository;
import com.test.task.repository.GroupSubjectRepository;
import com.test.task.repository.StudentRepository;
import com.test.task.service.implementations.GroupServiceImpl;
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
@DisplayName("<= GroupService Test =>")
public class GroupServiceTest {

  private final GroupRepository groupRepository;
  private final StudentRepository studentRepository;
  private final StudentService studentService;
  private final GroupService groupService;

  private Group firstGroup;
  private Group secondGroup;
  private List<Student> studentsForTest;

  @Autowired
  public GroupServiceTest(
      GroupRepository groupRepository,
      GroupSubjectRepository groupSubjectRepository,
      StudentRepository studentRepository
  ) {
    this.groupRepository = groupRepository;
    this.studentRepository = studentRepository;
    this.studentService = new StudentServiceImpl(studentRepository);
    this.groupService = new GroupServiceImpl(groupRepository,groupSubjectRepository,studentService);
  }

  @BeforeEach
  void setUp() {
    firstGroup = groupRepository.save(initGroup());
    secondGroup = groupRepository.save(initGroup());
    studentsForTest = IntStream
        .range(0, 15)
        .mapToObj(n -> studentRepository.save(initStudent(n < 9 ? firstGroup : secondGroup)))
        .collect(Collectors.toList());
  }

  @Test
  @DisplayName("<= find Group by id =>")
  void findGroupById() {
    Group group = groupService.findById(firstGroup.getId());
    assertAll(
        () -> assertThat(group).isEqualTo(firstGroup),
        () -> assertThat(group.getGroupName()).isEqualTo(firstGroup.getGroupName()),
        () -> assertThat(group.getSemesterOfStudy()).isEqualTo(firstGroup.getSemesterOfStudy())
    );
  }

  @Test
  @DisplayName("<= find Group by id throws Exception if doesn't exist =>")
  void findGroupByIdThrowException() {
    Long id = secondGroup.getId() + 1;
    assertThatThrownBy(() -> groupService.findById(id))
        .isInstanceOf(DataNotFoundException.class)
        .hasMessageContaining(id + "");
  }

  @Test
  @DisplayName("<= find all Group =>")
  void findAllGroup() {
    assertThat(groupService.findAll())
        .isEqualTo(List.of(firstGroup,secondGroup))
        .isEqualTo(groupRepository.findAll());
  }

  @Test
  @DisplayName("<= find all Group when empty =>")
  void findAllGroupWhenEmpty() {
    groupService.deleteById(firstGroup.getId());
    groupService.deleteById(secondGroup.getId());
    assertThat(groupService.findAll().size())
        .isEqualTo(0);
  }

  @Test
  @DisplayName("<= add new Group =>")
  void addGroup() {
    Group group = initGroup();

    Group toTest = groupService.add(group);
    assertAll(
        () -> assertThat(toTest.getId()).isNotNull(),
        () -> assertThat(groupRepository.findAll().size()).isEqualTo(3),
        () -> assertThat(groupRepository.findById(toTest.getId())).isNotEmpty()
    );
  }

  @Test
  @DisplayName("<= update Group =>")
  void updateGroup() {
    String name = secondGroup.getGroupName();
    secondGroup.setGroupName(name + "hello");

    Group toTest = groupService.update(secondGroup);
    assertAll(
        () -> assertThat(toTest).isEqualTo(secondGroup),
        () -> assertThat(groupRepository.findById(toTest.getId()).orElseThrow().getGroupName())
            .isNotEqualTo(name)
    );
  }

  @Test
  @DisplayName("<= delete Group by id =>")
  void deleteGroupById() {
    Long id = firstGroup.getId();
    groupService.deleteById(id);

    assertAll(
        () -> assertThat(groupService.findAll()).isEqualTo(List.of(secondGroup)),
        () -> assertThat(studentService.findById(studentsForTest.get(0).getId()).getGroup()).isNull()
    );
  }

  @Test
  @DisplayName("<= delete Group by id if doesn't exist =>")
  void deleteGroupByIdIfDoesntExist() {
    Long id = secondGroup.getId()+1;

    assertThatThrownBy(() -> groupService.deleteById(id)).hasMessageContaining(id + "");
  }

  @Test
  @DisplayName("<= add Student to Group =>")
  void addStudentToGroup(){
    Student student = studentRepository.save(initStudent(null));

    Group group = groupService.addStudentToGroup(student.getId(), firstGroup.getId());
    assertAll(
        ()->assertThat(student.getGroup()).isEqualTo(group)
    );
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
