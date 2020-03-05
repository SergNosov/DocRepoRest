package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4201")
public class DocumentController {
    private final DocumentService documentService;
    private final FileEntityService fileEntityService;

    @Autowired
    public DocumentController(DocumentService documentService,
                              FileEntityService fileEntityService) {
        this.documentService = documentService;
        this.fileEntityService = fileEntityService;
    }


    @GetMapping("/documents")
    public List<Document> getAllDocuments(){
        return documentService.findAll();
    }

    @GetMapping("/documents/{id}")
    public Document getDocument(@PathVariable int id){
        return  documentService.findById(id);
    }

    @PostMapping("/documents")
    public Document addDocument(@RequestBody @Valid Document document) {
        document.setId(0);
        return documentService.save(document);
    }

    @PutMapping("/documents")
    public Document updateDocument(@RequestBody @Valid Document document){
        if (document.getId() == 0) {
            throw new IllegalArgumentException("Неверное значение document.id." +
                    " При обновлении document.id  не должно быть равно 0.");
        }
        return documentService.save(document);
    }

    @DeleteMapping("/documents/{id}")
    public CommonMessage deleteDocument(@PathVariable int id) {
        int deletedId = documentService.deleteById(id);
        return new CommonMessage("Удален документ id - " + deletedId);
    }

    @GetMapping("/documents/files/{id}")
    public Set<FileEntity> getDocFiles(@PathVariable int id){
        Set<FileEntity> fileEntities = fileEntityService.findByDocId(id);
        return fileEntities;
    }
}
