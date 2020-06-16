package gov.kui.docRepoR.dao.impl;

import gov.kui.docRepoR.dao.FileEntityBlobAdditionalRepository;
import org.springframework.beans.factory.annotation.Autowired;


import javax.persistence.EntityManager;

public class FileEntityBlobAdditionalRepositoryImpl implements FileEntityBlobAdditionalRepository {

    private final EntityManager entityManager;

    @Autowired
    public FileEntityBlobAdditionalRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public int deleteFileEntityBlobById(int id) {
        return entityManager.createNamedQuery("deleteFileEntityBlobById")
                .setParameter("id", id)
                .executeUpdate();
    }
}
