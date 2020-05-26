package gov.kui.docRepoR.service;

import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.dto.DocumentDto;

public interface DocumentService extends BaseCrudService<Document> {

    public DocumentDto findDtoById(int id);
}
