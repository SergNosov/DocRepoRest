package gov.kui.docRepoR.dao.dtoRepository;

import gov.kui.docRepoR.dto.DoctypeDto;

import java.util.Optional;

public interface DoctypeDtoRepository {
    public DoctypeDto findDoctypeDtoById(int id);
}
