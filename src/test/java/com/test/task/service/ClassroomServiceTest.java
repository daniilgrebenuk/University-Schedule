package com.test.task.service;

import com.test.task.exception.DataNotFoundException;
import com.test.task.model.Classroom;
import com.test.task.repository.ClassroomRepository;
import com.test.task.service.implementations.ClassroomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("<= ClassroomService Test =>")
public class ClassroomServiceTest {

  private final ClassroomRepository classroomRepository;
  private final ClassroomService classroomService;

  private List<Classroom> classroomsForTest;

  @Autowired
  public ClassroomServiceTest(ClassroomRepository classroomRepository) {
    this.classroomRepository = classroomRepository;
    this.classroomService = new ClassroomServiceImpl(classroomRepository);
  }

  @BeforeEach
  void setUp() {
    classroomsForTest = IntStream
        .range(0,15)
        .mapToObj(n->classroomRepository.save(initClassroom()))
        .collect(Collectors.toList());
  }

  @Test
  @DisplayName("<= find Classroom by id =>")
  void findClassroomById() {
    Classroom excepted = classroomsForTest.get(0);
    Classroom actual = classroomService.findById(excepted.getId());
    assertAll(
        () -> assertThat(actual).isEqualTo(excepted),
        () -> assertThat(actual.getRoomNumber()).isEqualTo(excepted.getRoomNumber()),
        () -> assertThat(actual.getNumberOfSeats()).isEqualTo(excepted.getNumberOfSeats())
    );
  }

  @Test
  @DisplayName("<= find Classroom by id throws Exception if doesn't exist =>")
  void findClassroomByIdThrowException() {
    Long id = classroomsForTest.get(classroomsForTest.size()-1).getId() + 1;
    assertThatThrownBy(() -> classroomService.findById(id))
        .isInstanceOf(DataNotFoundException.class)
        .hasMessageContaining(id + "");
  }

  @Test
  @DisplayName("<= find all Classroom =>")
  void findAllClassroom() {
    assertThat(classroomService.findAll())
        .isEqualTo(classroomsForTest)
        .isEqualTo(classroomRepository.findAll());
  }

  @Test
  @DisplayName("<= find all Classroom when empty =>")
  void findAllClassroomWhenEmpty() {
    classroomRepository.deleteAll();
    assertThat(classroomService.findAll().size())
        .isEqualTo(0);
  }

  @Test
  @DisplayName("<= add new Classroom =>")
  void addClassroom() {
    Classroom classroom = initClassroom();

    Classroom toTest = classroomService.add(classroom);
    assertAll(
        () -> assertThat(toTest.getId()).isNotNull(),
        () -> assertThat(classroomRepository.findAll().size()).isEqualTo(classroomsForTest.size()+1),
        () -> assertThat(classroomRepository.findById(toTest.getId())).isNotEmpty()
    );
  }

  @Test
  @DisplayName("<= update Classroom =>")
  void updateClassroom() {
    Classroom classroom = classroomsForTest.get(0);
    Integer numberOfSeats = classroom.getNumberOfSeats();
    classroom.setNumberOfSeats(numberOfSeats+20);

    Classroom toTest = classroomService.update(classroom);
    assertAll(
        () -> assertThat(toTest).isEqualTo(classroom),
        () -> assertThat(classroomRepository.findById(toTest.getId()).orElseThrow().getNumberOfSeats())
            .isNotEqualTo(numberOfSeats).isEqualTo(numberOfSeats+20)
    );
  }

  @Test
  @DisplayName("<= delete Classroom by id =>")
  void deleteClassroomById() {
    Long id = classroomsForTest.get(0).getId();
    classroomService.deleteById(id);

    assertThat(classroomService.findAll())
        .isEqualTo(classroomsForTest.stream().filter(s->!s.getId().equals(id)).collect(Collectors.toList()));
  }

  @Test
  @DisplayName("<= delete Classroom by id if doesn't exist =>")
  void deleteClassroomByIdIfDoesntExist() {
    Long id = classroomsForTest.get(classroomsForTest.size()-1).getId() + 1;

    assertThatThrownBy(() -> classroomService.deleteById(id)).hasMessageContaining(id + "");
  }

  private Classroom initClassroom() {
    Classroom classroom = new Classroom();
    classroom.setNumberOfSeats((int)(Math.random()*3)+15);
    classroom.setRoomNumber((int)(Math.random()*400)+100);
    classroom.setGroupSubjects(new ArrayList<>());
    return classroom;
  }
}
