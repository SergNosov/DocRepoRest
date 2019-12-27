package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.model.FileEntity;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import gov.kui.docRepoR.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4201")
public class FileController {
    private FileEntityService fileEntityService;
    private DocumentService documentService;
    private FileStorageService fileStorageService;

    @Autowired
    public FileController(FileEntityService fileEntityService,
                          DocumentService documentService,
                          FileStorageService fileStorageService) {
        this.fileEntityService = fileEntityService;
        this.documentService = documentService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/files/{id}")
    public FileEntity uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file){

        documentService.findById(id);
        System.out.println("----- file (controller): "+file.getOriginalFilename()+ "; file size:"+ file.getSize());
        fileStorageService.storeFile(file);

        return null;
    }
}
