package com.test.task.repository;

import com.test.task.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

  String SQL_FIND_ALL_BY_GROUP_ID =
      "SELECT DISTINCT s.* " +
      "FROM subject s JOIN group_subject gs ON s.id_subject = gs.id_subject " +
      "WHERE gs.id_group = ?1";

  @Query(value = SQL_FIND_ALL_BY_GROUP_ID, nativeQuery = true)
  List<Subject> findAllByGroupId(Long idGroup);



}
