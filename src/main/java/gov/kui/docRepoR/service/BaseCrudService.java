package gov.kui.docRepoR.service;

import java.util.List;

public interface BaseCrudService<T> {

    List<T> findAll();

    T findById(int id);

    T save(T object);

    int deleteById(int id);
}
