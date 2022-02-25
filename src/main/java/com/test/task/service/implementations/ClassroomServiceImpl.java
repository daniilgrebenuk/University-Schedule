package com.test.task.service.implementations;

import com.test.task.exception.DataNotFoundException;
import com.test.task.model.Classroom;
import com.test.task.repository.ClassroomRepository;
import com.test.task.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

  private final ClassroomRepository classroomRepository;


  @Override
  public Classroom findById(Long id) {
    return classroomRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("Classroom with id: \"" + id + "\" doesn't exist!"));
  }

  @Override
  public List<Classroom> findAll() {
    return classroomRepository.findAll();
  }

  @Override
  public Classroom add(Classroom student) {
    return classroomRepository.save(student);
  }

  @Override
  public Classroom update(Classroom student) {
    return classroomRepository.save(student);
  }

  @Override
  public void deleteById(Long id) {
    classroomRepository.deleteById(id);
  }
}
