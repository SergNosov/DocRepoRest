package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.Doctype;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctypeRepository extends JpaRepository<Doctype,Integer> {
    public boolean existsByTitle(String title);
}
