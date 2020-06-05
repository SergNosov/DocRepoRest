package gov.kui.docRepoR.dao.dtoRepository;

import gov.kui.docRepoR.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileEntityRepository extends JpaRepository<FileEntity, Integer>, FileEntityDtoRepository {
}
