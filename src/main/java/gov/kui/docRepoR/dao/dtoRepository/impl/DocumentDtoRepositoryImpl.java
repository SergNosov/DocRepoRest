package gov.kui.docRepoR.dao.dtoRepository.impl;

import gov.kui.docRepoR.dao.dtoRepository.DocumentDtoRepository;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.SenderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Transactional(readOnly = true)
public class DocumentDtoRepositoryImpl implements DocumentDtoRepository {

    private final EntityManager entityManager;

    @Autowired
    public DocumentDtoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<DocumentDto> findDtoById(int id) {
        Optional<DocumentDto> documentDtoOptional = Optional.empty();

        try {
            DocumentDto documentDto = entityManager.createNamedQuery("DocumentDtoByDocId", DocumentDto.class)
                    .setParameter("docId", id)
                    .getSingleResult();
            setupDtoFields(documentDto);

            documentDtoOptional = Optional.of(documentDto);
        } catch (NoResultException nre) {
            log.error("--- DocumentDtoByDocId. docId = " + id + "; exception message: " + nre.toString());
        }

        return documentDtoOptional;
    }

    private void setupDtoFields(DocumentDto documentDto) {
        final int id = documentDto.getId();
        DoctypeDto doctypeDto = entityManager.createNamedQuery("DoctypeDtoByDocId", DoctypeDto.class)
                .setParameter("docId", id)
                .getSingleResult();
        documentDto.setDoctype(doctypeDto);

        Set<SenderDto> senderDtos = new HashSet<>(
                entityManager.createNamedQuery("SenderDtosByDocId", SenderDto.class)
                        .setParameter("docId", id)
                        .getResultList()
        );

        Set<FileEntityDto> fileEntityDtos = new HashSet<>(
                entityManager.createNamedQuery("FileEntityDtosByDocId", FileEntityDto.class)
                        .setParameter("docId", id)
                        .getResultList()
        );

        documentDto.setDoctype(doctypeDto);
        documentDto.setSenders(senderDtos);
        documentDto.setFileEntities(fileEntityDtos);
    }

    @Override
    public List<DocumentDto> findAllByPage(Pageable pagable) {
        final int indexfirstDto = (pagable.getPageNumber()-1)*pagable.getPageSize();
        List<DocumentDto> documentDtos = new ArrayList<>();

        documentDtos = entityManager.createNamedQuery("DocumentDtoByPage", DocumentDto.class)
                .setFirstResult(indexfirstDto)
                .setMaxResults(pagable.getPageSize())
                .getResultList();

        for (DocumentDto documentDto:documentDtos){
            setupDtoFields(documentDto);
        }
        return documentDtos;
    }
}
