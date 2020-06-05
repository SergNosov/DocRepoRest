package gov.kui.docRepoR.facade;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.FileEntityBlob;
import gov.kui.docRepoR.dto.FileEntityDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FileEntityServiceFacade {

    public FileEntityDto save(FileEntityBlob fileEntityBlob);

    public FileEntityDto findDtoById(int id);

    public List<FileEntityDto> findDtosByDocId(int id);

    public int deleteById(int id);

    public ResponseEntity<Resource> getResourseById(int id);
}
