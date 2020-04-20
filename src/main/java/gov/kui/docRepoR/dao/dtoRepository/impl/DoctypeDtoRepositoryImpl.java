package gov.kui.docRepoR.dao.dtoRepository.impl;

import gov.kui.docRepoR.dao.dtoRepository.DoctypeDtoRepository;
import gov.kui.docRepoR.dto.DoctypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional(readOnly = true)
public class DoctypeDtoRepositoryImpl implements DoctypeDtoRepository {

    private final EntityManager entityManager;

    @Autowired
    public DoctypeDtoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public DoctypeDto findDtoById(int id) {
        return entityManager.createNamedQuery("DoctypeDtoById", DoctypeDto.class)
                .setParameter("doctypeId", id)
                .getSingleResult();
    }

    @Override
    public List<DoctypeDto> findAllDtos() {
        return entityManager.createNamedQuery("DoctypeDtoAll", DoctypeDto.class).getResultList();
    }
}
