package gov.kui.docRepoR.dao.dtoRepository;

import gov.kui.docRepoR.dto.DoctypeDto;

import java.util.List;
import java.util.Optional;

public interface DoctypeDtoRepository {
    public Optional<DoctypeDto> findDtoById(int id);
    public List<DoctypeDto> findAllDtos();
}
