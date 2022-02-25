package com.test.task.service;

import com.test.task.model.Group;

public interface GroupService extends CrudService<Group, Long>{
  Group addStudentToGroup(Long idStudent, Long idGroup);
}
