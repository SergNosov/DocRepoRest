package gov.kui.docRepoR.service;

import gov.kui.docRepoR.model.FileEntity;

import java.util.List;

public interface FileEntityService {
    /**
     * getting all fileEntitys
     *
     * @return all fileEntitys from DB
     */
    public List<FileEntity> findAll();

    /**
     * getting specify fileEntity by ID
     *
     * @param id - fileEntity's id for receiving
     * @return fileEntity by id
     */
    public FileEntity findById(int id);

    /**
     * required for save or update fileEntity to db
     *
     * @param fileEntity - fileEntity for saving or updating
     * @return fileEntity
     */
    public FileEntity save(FileEntity document);

    /**
     * delete fileEntity  by ID
     *
     * @param id - fileEntity's id for deleting
     * @return id fileEntity
     */
    public int deleteById(int id);
}
