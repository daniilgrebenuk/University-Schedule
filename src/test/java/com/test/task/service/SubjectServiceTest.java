package com.test.task.service;

import com.test.task.exception.DataNotFoundException;
import com.test.task.model.Group;
import com.test.task.model.GroupSubject;
import com.test.task.model.Subject;
import com.test.task.repository.GroupRepository;
import com.test.task.repository.GroupSubjectRepository;
import com.test.task.repository.SubjectRepository;
import com.test.task.service.implementations.SubjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("<= SubjectService Test =>")
public class SubjectServiceTest {

  private final SubjectRepository subjectRepository;
  private final GroupSubjectRepository groupSubjectRepository;
  private final GroupRepository groupRepository;
  private final SubjectService subjectService;

  private List<Subject> subjectsForTest;

  @Autowired
  public SubjectServiceTest(
      SubjectRepository subjectRepository,
      GroupSubjectRepository groupSubjectRepository,
      GroupRepository groupRepository
  ) {
    this.subjectRepository = subjectRepository;
    this.groupSubjectRepository = groupSubjectRepository;
    this.groupRepository = groupRepository;
    this.subjectService = new SubjectServiceImpl(subjectRepository, groupSubjectRepository);
  }

  @BeforeEach
  void setUp() {
    subjectsForTest = IntStream
        .range(0,15)
        .mapToObj(n->subjectRepository.save(initSubject()))
        .collect(Collectors.toList());
  }

  @Test
  @DisplayName("<= find Subject by id =>")
  void findSubjectById() {
    Subject excepted = subjectsForTest.get(0);
    Subject actual = subjectService.findById(excepted.getId());
    assertAll(
        () -> assertThat(actual).isEqualTo(excepted),
        () -> assertThat(actual.getName()).isEqualTo(excepted.getName())
    );
  }

  @Test
  @DisplayName("<= find Subject by id throws Exception if doesn't exist =>")
  void findSubjectByIdThrowException() {
    Long id = subjectsForTest.get(subjectsForTest.size()-1).getId() + 1;
    assertThatThrownBy(() -> subjectService.findById(id))
        .isInstanceOf(DataNotFoundException.class)
        .hasMessageContaining(id + "");
  }

  @Test
  @DisplayName("<= find all Subject =>")
  void findAllSubject() {
    assertThat(subjectService.findAll())
        .isEqualTo(subjectsForTest)
        .isEqualTo(subjectRepository.findAll());
  }

  @Test
  @DisplayName("<= find all Subject when empty =>")
  void findAllSubjectWhenEmpty() {
    subjectRepository.deleteAll();
    assertThat(subjectService.findAll().size())
        .isEqualTo(0);
  }

  @Test
  @DisplayName("<= add new Subject =>")
  void addSubject() {
    Subject subject = initSubject();

    Subject toTest = subjectService.add(subject);
    assertAll(
        () -> assertThat(toTest.getId()).isNotNull(),
        () -> assertThat(subjectRepository.findAll().size()).isEqualTo(subjectsForTest.size()+1),
        () -> assertThat(subjectRepository.findById(toTest.getId())).isNotEmpty()
    );
  }

  @Test
  @DisplayName("<= update Subject =>")
  void updateSubject() {
    Subject subject = subjectsForTest.get(0);
    String name = subject.getName();
    subject.setName(name + "hello");

    Subject toTest = subjectService.update(subject);
    assertAll(
        () -> assertThat(toTest).isEqualTo(subject),
        () -> assertThat(subjectRepository.findById(toTest.getId()).orElseThrow().getName())
            .isNotEqualTo(name)
    );
  }

  @Test
  @DisplayName("<= delete Subject by id =>")
  void deleteSubjectById() {
    Long id = subjectsForTest.get(0).getId();
    subjectService.deleteById(id);

    assertThat(subjectService.findAll())
        .isEqualTo(subjectsForTest.stream().filter(s->!s.getId().equals(id)).collect(Collectors.toList()));
  }

  @Test
  @DisplayName("<= delete Subject by id if doesn't exist =>")
  void deleteSubjectByIdIfDoesntExist() {
    Long id = subjectsForTest.get(subjectsForTest.size()-1).getId() + 1;

    assertThatThrownBy(() -> subjectService.deleteById(id)).hasMessageContaining(id + "");
  }

  @Test
  @DisplayName("<= find all Subject by Group id =>")
  void findAllSubjectByGroupId(){
    Group group = groupRepository.save(initGroup());
    List<Subject> subjectWithGroup = subjectsForTest.subList(0,7);
    subjectWithGroup.forEach(s->{
      for (int i = 0; i < (int) (Math.random()*4)+1; i++) {
        GroupSubject groupSubject = new GroupSubject();
        groupSubject.getId().setDate(LocalDate.of(i+2021,i+1,i+1));

        groupSubject.setGroup(group);
        groupSubject.setSubject(s);
        groupSubjectRepository.save(groupSubject);
      }
    });

    assertThat(subjectService.findAllByGroupId(group.getId())).isEqualTo(subjectWithGroup);
  }

  private Group initGroup() {
    Group group = new Group();
    group.setSemesterOfStudy((int) (Math.random() * 7) + 1);
    group.setGroupName("" + (int) (Math.random() * 1000));
    return group;
  }

  private Subject initSubject(){
    Subject subject = new Subject();
    subject.setName("" + (int) (Math.random() * 1000));
    return subject;
  }
}
