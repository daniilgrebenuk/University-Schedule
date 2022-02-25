package com.test.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "group_subject")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class GroupSubject {

  @EmbeddedId
  private GroupSubjectKey id = new GroupSubjectKey();


  @ManyToOne
  @MapsId("idGroup")
  @JoinColumn(name = "id_group")
  @ToString.Exclude
  @JsonIgnore
  private Group group;

  @ManyToOne
  @MapsId("idSubject")
  @JoinColumn(name = "id_subject")
  @ToString.Exclude
  @JsonIgnore
  private Subject subject;

  @ManyToOne
  @JoinColumn(name = "id_classroom")
  private Classroom classroom;

  public Schedule getSchedule() {
    return new Schedule() {
      @Override
      public String getSubjectName() {

        return subject.getName();
      }

      @Override
      public String getGroupName() {
        return group.getGroupName();
      }

      @Override
      public Integer getClassroomNumber() {
        return classroom == null ? null : classroom.getRoomNumber();
      }

      @Override
      public LocalDate getDate() {
        return id.getDate();
      }
    };
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    GroupSubject that = (GroupSubject) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
