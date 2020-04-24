package gov.kui.docRepoR.dao.dtoRepository;

import gov.kui.docRepoR.dto.FileEntityDto;

import java.util.List;
import java.util.Optional;

public interface FileEntityDtoRepository {
    public Optional<FileEntityDto> findDtoById(int id);
    public List<FileEntityDto> findFileEntityDtosByDocId(int docId);
}
