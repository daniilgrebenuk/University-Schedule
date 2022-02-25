package com.test.task.service.implementations;

import com.test.task.exception.DataNotFoundException;
import com.test.task.model.Student;
import com.test.task.repository.StudentRepository;
import com.test.task.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;

  @Override
  public Student findById(Long id) {
    return studentRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("Student with id: \"" + id + "\" doesn't exist!"));
  }

  @Override
  public List<Student> findAll() {
    return studentRepository.findAll();
  }

  @Override
  public Student add(Student student) {
    return studentRepository.save(student);
  }

  @Override
  public Student update(Student student) {
    findById(student.getId());
    return studentRepository.save(student);
  }


  @Override
  public void deleteById(Long id) {
    studentRepository.deleteById(id);
  }

  @Override
  public List<Student> findAllByGroupId(Long groupId) {
    return studentRepository.findAllByGroup_Id(groupId);
  }
}
