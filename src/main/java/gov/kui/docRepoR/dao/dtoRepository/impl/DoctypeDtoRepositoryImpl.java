package gov.kui.docRepoR.dao.dtoRepository.impl;

import gov.kui.docRepoR.dao.dtoRepository.DoctypeDtoRepository;
import gov.kui.docRepoR.dto.DoctypeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
public class DoctypeDtoRepositoryImpl implements DoctypeDtoRepository {

    private final EntityManager entityManager;

    @Autowired
    public DoctypeDtoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<DoctypeDto> findDtoById(int id) {
        Optional<DoctypeDto> dtoOptional = Optional.empty();

        try {
            dtoOptional = Optional.of(entityManager.createNamedQuery("DoctypeDtoById", DoctypeDto.class)
                    .setParameter("doctypeId", id)
                    .getSingleResult());
        } catch (NoResultException nre) {
            log.error("--- id = " + id + "; exception message: " + nre.getMessage());
        }

        return dtoOptional;
    }

    @Override
    public List<DoctypeDto> findAllDtos() {
        return entityManager.createNamedQuery("DoctypeDtoAll", DoctypeDto.class).getResultList();
    }
}
