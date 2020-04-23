package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.dao.dtoRepository.SenderDtoRepository;
import gov.kui.docRepoR.domain.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenderRepository extends JpaRepository<Sender,Integer>, SenderDtoRepository {
    public boolean existsByTitle(String title);
}
