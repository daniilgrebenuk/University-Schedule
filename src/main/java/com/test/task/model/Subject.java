package com.test.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Subject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_subject")
  private Long id;

  private String name;

  @OneToMany(mappedBy = "subject")
  @ToString.Exclude
  @JsonIgnore
  private List<GroupSubject> subjects;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Subject subject = (Subject) o;
    return id != null && Objects.equals(id, subject.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
