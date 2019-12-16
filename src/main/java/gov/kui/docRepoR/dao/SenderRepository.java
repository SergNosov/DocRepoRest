package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.model.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenderRepository extends JpaRepository<Sender,Integer> {
    public boolean existsByTitle(String title);
}
