package gov.kui.docRepoR.service;

import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.dto.DocumentDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocumentService extends BaseCrudService<Document> {

    public List<DocumentDto> findAllDtos();
    public List<DocumentDto> findAllDtosByPage(Pageable pagable);
    public DocumentDto findDtoById(int id);
}
