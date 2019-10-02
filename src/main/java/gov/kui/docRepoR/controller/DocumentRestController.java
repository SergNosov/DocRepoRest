package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.Entity.Document;
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
    public List<Document> getAllDocuments(){
        return documentService.findAll();
    }

    @GetMapping("/documents/{id}")
    public Document getDoctype(@PathVariable int id){
        return  documentService.findById(id);
    }
}
