package gov.kui.docRepoR.facade;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BaseFacade<T> {

    List<T> findAll();
    List<T> findAllByPage(Pageable pagable);
    T findById(int id);
    T save(T object);
    T update(T object);
    int deleteById(int id);
}
