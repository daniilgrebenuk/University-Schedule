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
@Table(name = "groups")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Group {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_group")
  private Long id;

  private String groupName;

  private Integer semesterOfStudy;

  @OneToMany(mappedBy = "group")
  @ToString.Exclude
  @JsonIgnore
  private List<GroupSubject> subjects;

  @OneToMany(mappedBy = "group")
  @ToString.Exclude
  @JsonIgnore
  private List<Student> students;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Group group = (Group) o;
    return id != null && Objects.equals(id, group.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
