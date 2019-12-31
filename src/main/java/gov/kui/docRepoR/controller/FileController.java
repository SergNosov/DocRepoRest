package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.model.FileEntity;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import gov.kui.docRepoR.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
    public FileEntity uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        documentService.findById(id);
        String filename = fileStorageService.storeFile(file);

        if (filename == null) {
            throw new RuntimeException("Ошибка загрузки файла. filename: " + filename + "; file: " + file.getName());
        }

        FileEntity fileEntity = new FileEntity(filename, file.getSize(), id);
        fileEntityService.save(fileEntity);

        return fileEntity;
    }

    @GetMapping("/files/{id}")
    public FileEntity getFileEntity(@PathVariable int id) {
        FileEntity fileEntity = fileEntityService.findById(id);
        return fileEntity;
    }

    @GetMapping("/files/load/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable int id, HttpServletRequest request)  {
        FileEntity fileEntity = fileEntityService.findById(id);
        Resource resource = fileStorageService.loadFileAsResource(
                fileEntity.getFilename()
        );

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            System.out.println("---- contentType: "+contentType);
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() + "\"")
                .body(resource);
    }
}
