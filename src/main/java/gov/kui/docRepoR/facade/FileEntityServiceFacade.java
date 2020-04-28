package gov.kui.docRepoR.facade;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;

import java.util.List;

public interface FileEntityServiceFacade {

    public FileEntityDto save(FileEntity fileEntity);

    public FileEntityDto findById(int id);

    public List<FileEntityDto> findDtosByDocId(int id);

    public int deleteById(int id);
}
