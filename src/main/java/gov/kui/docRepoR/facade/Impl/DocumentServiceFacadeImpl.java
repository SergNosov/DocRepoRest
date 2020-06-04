package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.dto.mappers.DoctypeMapper;
import gov.kui.docRepoR.dto.mappers.DocumentMapper;
import gov.kui.docRepoR.facade.DocumentServiceFacade;
import gov.kui.docRepoR.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
public class DocumentServiceFacadeImpl implements DocumentServiceFacade {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @Autowired
    public DocumentServiceFacadeImpl(DocumentService documentService, DocumentMapper documentMapper) {
        this.documentService = documentService;
        this.documentMapper = documentMapper;
    }

    @Override
    public List<DocumentDto> findAll() {
        return documentService.findAllDtos();
    }

    @Override
    public List<DocumentDto> findAllByPage(Pageable pagable) {
        Assert.notNull(pagable, "pagable is null.");
        return documentService.findAllDtosByPage(pagable);
    }

    @Override
    public DocumentDto findById(int id) {
        return documentService.findDtoById(id);
    }

    @Override
    public DocumentDto save(DocumentDto documentDto) {
        Assert.notNull(documentDto, "Не указан documentDto (null)");
        documentDto.setId(0);
        return saveOrUpdate(documentDto);
    }

    @Override
    public DocumentDto update(DocumentDto documentDto) {
        Assert.notNull(documentDto, "Не указан documentDto (null)");
        return saveOrUpdate(documentDto);
    }

    @Override
    public int deleteById(int id) {
        return documentService.deleteById(id);
    }

    private DocumentDto saveOrUpdate(DocumentDto documentDto) {
        Document document = documentService.save(
                documentMapper.documentDtoToDocument(documentDto)
        );
        Assert.notNull(document, "Не получен документ из сервиса documentService (null)");
        return documentMapper.documentToDocumentDto(document);
    }
}
