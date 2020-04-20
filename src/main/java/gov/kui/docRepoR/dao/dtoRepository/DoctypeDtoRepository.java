package gov.kui.docRepoR.dao.dtoRepository;

import gov.kui.docRepoR.dto.DoctypeDto;

import java.util.List;

public interface DoctypeDtoRepository {
    public DoctypeDto findDtoById(int id);
    public List<DoctypeDto> findAllDtos();
}
