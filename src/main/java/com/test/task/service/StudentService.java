package com.test.task.service;

import com.test.task.model.Student;

import java.util.List;

public interface StudentService extends CrudService<Student, Long>{
  List<Student> findAllByGroupId(Long groupId);
}
