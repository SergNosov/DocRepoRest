package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileEntityRepository extends JpaRepository<FileEntity,Integer> {
}
