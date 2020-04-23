package gov.kui.docRepoR.dao.dtoRepository;

import gov.kui.docRepoR.dto.SenderDto;

import java.util.List;
import java.util.Optional;

public interface SenderDtoRepository {
    public Optional<SenderDto> findDtoById(int id);
    public List<SenderDto> findAllDtos();
}
