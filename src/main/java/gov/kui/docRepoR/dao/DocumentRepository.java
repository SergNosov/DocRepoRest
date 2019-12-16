package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Integer> {
}
