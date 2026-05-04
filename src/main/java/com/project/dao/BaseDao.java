package com.project.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
    List<T> findAll();

    Optional<T> add(T model);
}
