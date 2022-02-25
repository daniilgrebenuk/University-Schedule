package com.test.task.service.implementations;

import com.test.task.exception.DataNotFoundException;
import com.test.task.model.Group;
import com.test.task.model.Student;
import com.test.task.repository.GroupRepository;
import com.test.task.repository.GroupSubjectRepository;
import com.test.task.service.GroupService;
import com.test.task.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

  private final GroupRepository groupRepository;
  private final GroupSubjectRepository groupSubjectRepository;
  private final StudentService studentService;


  @Override
  public Group findById(Long id) {
    return groupRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("Group with id: \"" + id + "\" doesn't exist!"));
  }

  @Override
  public List<Group> findAll() {
    return groupRepository.findAll();
  }

  @Override
  public Group add(Group student) {
    return groupRepository.save(student);
  }

  @Override
  public Group update(Group student) {
    return groupRepository.save(student);
  }

  @Override
  public void deleteById(Long id) {
    studentService.findAllByGroupId(id).forEach(s->{
      s.setGroup(null);
      studentService.update(s);
    });
    groupSubjectRepository.deleteByGroupId(id);
    groupRepository.deleteById(id);
  }

  @Override
  public Group addStudentToGroup(Long idStudent, Long idGroup) {
    Group group = findById(idGroup);
    Student student = studentService.findById(idStudent);

    student.setGroup(group);
    studentService.update(student);
    return groupRepository.save(group);
  }
}
