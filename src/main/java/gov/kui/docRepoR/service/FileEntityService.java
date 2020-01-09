package gov.kui.docRepoR.service;

import gov.kui.docRepoR.model.FileEntity;

import java.util.List;
import java.util.Set;

public interface FileEntityService  extends BaseCrudService<FileEntity>{
    /**
     * getting specify fileEntity by document ID
     *
     * @param id - document ID for receiving
     * @return Set<fileEntity>
     */
    public Set<FileEntity> findByDocId(int id);
}
