package com.test.task.service;

import com.test.task.model.Subject;

import java.util.List;

public interface SubjectService extends CrudService<Subject, Long> {
  List<Subject> findAllByGroupId(Long groupId);
}
