package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.facade.DocumentServiceFacade;
import gov.kui.docRepoR.facade.FileEntityServiceFacade;
import gov.kui.docRepoR.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentController {
    private final DocumentService documentService;
    private final FileEntityServiceFacade fileEntityFacade;
    private final DocumentServiceFacade documentFacade;

    @Autowired
    public DocumentController(DocumentService documentService,
                              FileEntityServiceFacade fileEntityFacade,
                              DocumentServiceFacade documentFacade) {
        this.documentService = documentService;
        this.fileEntityFacade = fileEntityFacade;
        this.documentFacade = documentFacade;
    }

    @GetMapping("/documents")
    public List<DocumentDto> getAllDocuments() {
        return documentFacade.findAll();
    }

    @GetMapping("/documents/{page}/{size}")
    public List<DocumentDto> getAllDocumentsByPages(@PathVariable int page, @PathVariable int size){
        Pageable pageable = PageRequest.of(page, size);
        return documentFacade.findAllByPage(pageable);
    }

    @GetMapping("/documents/{id}")
    public DocumentDto getDocument(@PathVariable int id) {
            return documentFacade.findById(id);
    }

    @PostMapping("/documents")
    public DocumentDto addDocument(@RequestBody @Valid DocumentDto documentDto) {
        documentDto.setId(0);
        return documentFacade.save(documentDto);
    }

    @PutMapping("/documents")
    public DocumentDto updateDocument(@RequestBody @Valid DocumentDto documentDto) {
        if (documentDto.getId() == 0) {
            throw new IllegalArgumentException("Неверное значение document.id." +
                    " При обновлении document.id  не должно быть равно 0.");
        }
        return documentFacade.update(documentDto);
    }

    @DeleteMapping("/documents/{id}")
    public CommonMessage deleteDocument(@PathVariable int id) {
        int deletedId = documentFacade.deleteById(id);
        return new CommonMessage("Удален документ id - " + deletedId);
    }

    @GetMapping("/documents/files/{id}")
    public List<FileEntityDto> getDocumentFiles(@PathVariable int id) {
        List<FileEntityDto> fileEntities = fileEntityFacade.findDtosByDocId(id);
        return fileEntities;
    }
}
