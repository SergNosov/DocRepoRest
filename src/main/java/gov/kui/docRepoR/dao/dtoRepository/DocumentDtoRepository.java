package gov.kui.docRepoR.dao.dtoRepository;

import gov.kui.docRepoR.dto.DocumentDto;

import java.util.Optional;

public interface DocumentDtoRepository {
    public Optional<DocumentDto> findDtoById(int id);
}
