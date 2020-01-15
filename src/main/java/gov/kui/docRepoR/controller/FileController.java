package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.model.CommonMessage;
import gov.kui.docRepoR.model.FileEntity;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import gov.kui.docRepoR.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:4201")
public class FileController {
    private final FileEntityService fileEntityService;
    private final DocumentService documentService;
    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileEntityService fileEntityService,
                          DocumentService documentService,
                          FileStorageService fileStorageService) {
        this.fileEntityService = fileEntityService;
        this.documentService = documentService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/{docId}")
    public FileEntity uploadFile(@PathVariable int docId, @RequestParam("file") MultipartFile file) {
        documentService.findById(docId);
        FileEntity fileEntity = createEntity(file);
        fileEntity.setDocumentId(docId);

        return fileEntityService.save(fileEntity);
    }

    @GetMapping("/{id}")
    public FileEntity getFileEntity(@PathVariable int id) {
        FileEntity fileEntity = fileEntityService.findById(id);
        return fileEntity;
    }

    @DeleteMapping("/{id}")
    public CommonMessage deleteFileEntity(@PathVariable int id) {
        int deletedId = fileEntityService.deleteById(id);
        return new CommonMessage("Удален файл id - " + deletedId);
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable int id, HttpServletRequest request) {

        FileEntity fileEntity = fileEntityService.findById(id);
        Resource resource = new ByteArrayResource(fileEntity.getData());
        String contentType = fileEntity.getContentType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                fileEntity.getFilename() + "\"")
                .body(resource);
    }

    private FileEntity createEntity(MultipartFile file) {
        try {
            FileEntity fileEntity = new FileEntity(file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    0);
            fileEntity.setData(file.getBytes());

            return fileEntity;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла. file: " + file.getName() + "; " + e.getMessage());
        }
    }
}
