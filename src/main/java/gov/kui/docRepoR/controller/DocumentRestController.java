package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentRestController {
    private DocumentService documentService;

    @Autowired
    public DocumentRestController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/documents")
    public List<DocumentDto> getAllDocuments(){
        return documentService.findAll();
    }

    @GetMapping("/documents/{id}")
    public DocumentDto getDoctype(@PathVariable int id){
        DocumentDto documentDto = documentService.findById(id);
        return  documentDto;
    }
}
