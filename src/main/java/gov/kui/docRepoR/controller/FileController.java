package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
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

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:4201")
public class FileController {
    private final FileEntityService fileEntityService;
    private final DocumentService documentService;

    @Autowired
    public FileController(FileEntityService fileEntityService,
                          DocumentService documentService) {
        this.fileEntityService = fileEntityService;
        this.documentService = documentService;
    }

    @PostMapping("/{docId}")
    public FileEntity uploadFile(@PathVariable int docId, @RequestParam("file") MultipartFile file) {

        Document doc = documentService.findById(docId);
        FileEntity fileEntity = FileEntity.getInstance(file, doc.getId());
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
    public ResponseEntity<Resource> getFile(@PathVariable int id) {

        ResponseEntity<Resource> responseEntity = ResponseEntity.noContent().build();
        FileEntity fileEntity = fileEntityService.findById(id);

        if (fileEntity.getBytes() != null) {
            Resource resource = new ByteArrayResource(fileEntity.getBytes());
            String contentType = fileEntity.getContentType();

            responseEntity = ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    fileEntity.getFilename() + "\"")
                    .body(resource);
        }
        return responseEntity;
    }
}
