package gov.kui.docRepoR.facade;

import java.util.List;

public interface BaseFacade<T> {

    List<T> findAll();
    T findById(int id);
    T save(T object);
    T update(T object);
    int deleteById(int id);
}
