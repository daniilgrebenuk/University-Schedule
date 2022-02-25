package com.test.task.repository;

import com.test.task.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

  List<Student> findAllByGroup_Id(Long idGroup);
}
