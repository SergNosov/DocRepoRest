package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.dao.dtoRepository.DocumentDtoRepository;
import gov.kui.docRepoR.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Integer>, DocumentDtoRepository {
}
