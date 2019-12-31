package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FileEntityRepository extends JpaRepository<FileEntity,Integer> {
    Set<FileEntity> findAllByDocumentId(int docId);
}
