package com.test.task.model;

import java.time.LocalDate;

public interface Schedule {

  String getSubjectName();

  String getGroupName();

  Integer getClassroomNumber();

  LocalDate getDate();
}
