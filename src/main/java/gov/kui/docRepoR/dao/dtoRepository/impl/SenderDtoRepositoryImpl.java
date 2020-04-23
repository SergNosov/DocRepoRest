package gov.kui.docRepoR.dao.dtoRepository.impl;

import gov.kui.docRepoR.dao.dtoRepository.SenderDtoRepository;
import gov.kui.docRepoR.dto.SenderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
public class SenderDtoRepositoryImpl implements SenderDtoRepository {

    private final EntityManager entityManager;

    @Autowired
    public SenderDtoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<SenderDto> findDtoById(int id) {
        Optional<SenderDto> dtoOptional = Optional.empty();

        try {
            dtoOptional = Optional.of(entityManager.createNamedQuery("SenderDtoById", SenderDto.class)
                    .setParameter("senderId", id)
                    .getSingleResult());
        } catch (NoResultException nre) {
            log.error("---- id = " + id + "; exception message: " + nre.getMessage());
        }

        return dtoOptional;
    }

    @Override
    public List<SenderDto> findAllDtos() {
        return entityManager.createNamedQuery("SenderDtoAll", SenderDto.class).getResultList();
    }
}
