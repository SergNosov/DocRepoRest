package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.FileEntityBlob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileEntityBlobRepository extends JpaRepository<FileEntityBlob, Integer> {
}
