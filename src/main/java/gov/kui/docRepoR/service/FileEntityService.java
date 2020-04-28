package gov.kui.docRepoR.service;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;

import java.util.List;

public interface FileEntityService extends BaseCrudService<FileEntity> {
    /**
     * getting specify fileEntityDtos by document ID
     *
     * @param id - document ID for receiving
     * @return List<FileEntityDto>
     */
    public List<FileEntityDto> findDtosByDocId(int id);

    /**
     * getting specify fileEntityDto by ID
     *
     * @param id - fileEntity ID for receiving
     * @return FileEntityDto
     */
    public FileEntityDto findDtoById(int id);
}
