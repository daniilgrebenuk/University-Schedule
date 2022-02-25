package com.test.task.service;

import java.util.List;

public interface CrudService <T, K>{
  T findById(K id);

  List<T> findAll();

  T add(T student);

  T update(T student);

  void deleteById(K id);
}
