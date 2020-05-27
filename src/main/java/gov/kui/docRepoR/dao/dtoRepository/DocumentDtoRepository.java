package gov.kui.docRepoR.dao.dtoRepository;

import gov.kui.docRepoR.dto.DocumentDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DocumentDtoRepository {
    public Optional<DocumentDto> findDtoById(int id);
    public List<DocumentDto> findAllByPage(Pageable pagable);
}
