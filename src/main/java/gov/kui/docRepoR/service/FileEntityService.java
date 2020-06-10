package gov.kui.docRepoR.service;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.FileEntityBlob;
import gov.kui.docRepoR.dto.FileEntityDto;

import java.util.List;

public interface FileEntityService  {

    /**
     * deleting specify FileEntityBlob by ID
     *
     * @param id - ID for deleting
     * @return id deleted FileEntityBlob
     */
    public int deleteById(int id);

    /**
     * getting specify FileEntityBlob by ID
     *
     * @param id - ID for receiving
     * @return FileEntityBlob
     */
    public FileEntityBlob findById(int id);

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

    /**
     * required for save FileEntityBlob to db
     *
     * @param fileEntityBlob - entity for saving
     * @return saved FileEntity without blob field
     */
    public FileEntity save(final FileEntityBlob fileEntityBlob);
}
