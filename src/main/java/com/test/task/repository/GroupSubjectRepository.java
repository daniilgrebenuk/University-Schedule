package com.test.task.repository;

import com.test.task.model.GroupSubject;
import com.test.task.model.GroupSubjectKey;
import com.test.task.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface GroupSubjectRepository extends JpaRepository<GroupSubject, GroupSubjectKey> {

  String SQL_FIND_ALL_BY_STUDENT_ID_AND_DATE =
  "SELECT sb.name as subjectName, g.group_name as groupName , c.room_number as classroomNumber, gs.date " +
  "FROM student s JOIN groups g ON s.id_group = g.id_group " +
                 "JOIN group_subject gs ON g.id_group = gs.id_group " +
                 "JOIN subject sb ON gs.id_subject = sb.id_subject " +
                 "JOIN classroom c ON gs.id_classroom = c.id_classroom " +
  "WHERE s.id_student = ?1 AND gs.date = ?2";

  String SQL_FIND_ALL_BY_GROUP_ID_AND_DATE =
      "SELECT sb.name as subjectName, g.group_name as groupName , c.room_number as classroomNumber, gs.date " +
      "FROM groups g JOIN group_subject gs ON g.id_group = gs.id_group " +
                    "JOIN subject sb ON gs.id_subject = sb.id_subject " +
                    "JOIN classroom c ON gs.id_classroom = c.id_classroom " +
      "WHERE g.id_group = ?1 AND gs.date = ?2";

  @Modifying
  @Transactional
  @Query("DELETE FROM GroupSubject WHERE id.idSubject = ?1")
  void deleteBySubjectId(Long idSubject);

  @Modifying
  @Transactional
  @Query("DELETE FROM GroupSubject WHERE id.idGroup = ?1")
  void deleteByGroupId(Long idGroup);


  @Query(value = SQL_FIND_ALL_BY_STUDENT_ID_AND_DATE, nativeQuery = true)
  List<Schedule> findAllScheduleByStudentIdAndDate(Long idStudent, LocalDate date);

  @Query(value = SQL_FIND_ALL_BY_GROUP_ID_AND_DATE, nativeQuery = true)
  List<Schedule> findAllScheduleByGroupIdAndDate(Long idGroup, LocalDate date);
}
