package com.test.task.service.implementations;

import com.test.task.exception.DataNotFoundException;
import com.test.task.model.Subject;
import com.test.task.repository.GroupSubjectRepository;
import com.test.task.repository.SubjectRepository;
import com.test.task.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

  private final SubjectRepository subjectRepository;
  private final GroupSubjectRepository groupSubjectRepository;

  @Override
  public Subject findById(Long id) {
    return subjectRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("Subject with id: \"" + id + "\" doesn't exist!"));
  }

  @Override
  public List<Subject> findAll() {
    return subjectRepository.findAll();
  }

  @Override
  public Subject add(Subject subject) {
    return subjectRepository.save(subject);
  }

  @Override
  public Subject update(Subject subject) {
    findById(subject.getId());
    return subjectRepository.save(subject);
  }

  @Override
  public void deleteById(Long id) {
    groupSubjectRepository.deleteBySubjectId(id);
    subjectRepository.deleteById(id);
  }

  @Override
  public List<Subject> findAllByGroupId(Long idGroup) {
    return subjectRepository.findAllByGroupId(idGroup);
  }
}
