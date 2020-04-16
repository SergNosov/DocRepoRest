package gov.kui.docRepoR.dao.dtoRepository.impl;

import gov.kui.docRepoR.dao.dtoRepository.DoctypeDtoRepository;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.mappers.DoctypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional(readOnly = true)
public class DoctypeDtoRepositoryImpl implements DoctypeDtoRepository {

    private final EntityManager entityManager;
    private final DoctypeMapper doctypeMapper;

    @Autowired
    public DoctypeDtoRepositoryImpl(EntityManager entityManager, DoctypeMapper doctypeMapper) {
        this.entityManager = entityManager;
        this.doctypeMapper = doctypeMapper;
    }

    @Override
    public DoctypeDto findDoctypeDtoById(int id) {
        DoctypeDto doctypeDto = (DoctypeDto) entityManager.createNamedQuery("DoctypeDto")
                .setParameter("doctypeId", id)
                .getSingleResult();
        return doctypeDto;
    }
}
