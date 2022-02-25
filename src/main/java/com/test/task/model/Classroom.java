package com.test.task.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Classroom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_classroom")
  private Long id;

  private Integer roomNumber;
  private Integer numberOfSeats;

  @OneToMany(mappedBy = "classroom")
  @ToString.Exclude
  private List<GroupSubject> groupSubjects;

  @PreRemove
  private void preRemove(){
    groupSubjects.forEach(gs->gs.setClassroom(null));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Classroom classroom = (Classroom) o;
    return id != null && Objects.equals(id, classroom.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
