package com.test.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupSubjectKey implements Serializable {

  @Column(name = "id_group")
  private Long idGroup;

  @Column(name = "id_subject")
  private Long idSubject;

  @Column(name = "date")
  private LocalDate date;


  public GroupSubjectKey(GroupSubjectKey key) {
    this.idGroup = key.getIdGroup();
    this.idSubject = key.getIdSubject();
    this.date = key.getDate();
  }
}
