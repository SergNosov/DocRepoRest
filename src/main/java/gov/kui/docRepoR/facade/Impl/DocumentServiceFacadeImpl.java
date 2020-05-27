package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.facade.DocumentServiceFacade;
import gov.kui.docRepoR.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceFacadeImpl implements DocumentServiceFacade {

    private final DocumentService documentService;

    @Autowired
    public DocumentServiceFacadeImpl(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Override
    public List<DocumentDto> findAll() {
        return null;
    }

    @Override
    public DocumentDto findById(int id) {
        return documentService.findDtoById(id);
    }

    @Override
    public DocumentDto save(DocumentDto object) {
        return null;
    }

    @Override
    public DocumentDto update(DocumentDto object) {
        return null;
    }

    @Override
    public int deleteById(int id) {
        return 0;
    }
}
