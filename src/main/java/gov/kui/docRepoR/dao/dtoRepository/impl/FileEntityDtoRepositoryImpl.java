package gov.kui.docRepoR.dao.dtoRepository.impl;

import gov.kui.docRepoR.dao.dtoRepository.FileEntityDtoRepository;
import gov.kui.docRepoR.dto.FileEntityDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FileEntityDtoRepositoryImpl implements FileEntityDtoRepository {

    private final EntityManager entityManager;

    @Autowired
    public FileEntityDtoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<FileEntityDto> findDtoById(int id) {
        Optional<FileEntityDto> dtoOptional = Optional.empty();

        try {
            dtoOptional = Optional.of(entityManager.createNamedQuery("FileEntityDtoById", FileEntityDto.class)
                    .setParameter("fileId", id)
                    .getSingleResult());
        } catch (NoResultException nre) {
            log.error("---- id = " + id + "; exception message: " + nre.getMessage());
        }
        return dtoOptional;
    }

    @Override
    public List<FileEntityDto> findFileEntityDtosByDocId(int docId) {
        return entityManager.createNamedQuery("FileEntityDtosByDocId", FileEntityDto.class)
                .setParameter("documentId", docId)
                .getResultList();
    }
}
