package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.model.CommonMessage;
import gov.kui.docRepoR.model.FileEntity;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import gov.kui.docRepoR.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

    @PostMapping("/{id}")
    public FileEntity uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file) {

        documentService.findById(id);
        try {
            FileEntity fileEntity = new FileEntity(file.getOriginalFilename(), file.getSize(), id);
            fileEntity.setData(file.getBytes());
            fileEntity.setData(null);
            fileEntityService.save(fileEntity);
            return fileEntity;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла. file: " + file.getName() + "; " + e.getMessage());
        }
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
        Resource resource = fileStorageService.loadFileAsResource(
                fileEntity.getFilename()
        );

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            System.out.println("---- contentType: " + contentType);
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
