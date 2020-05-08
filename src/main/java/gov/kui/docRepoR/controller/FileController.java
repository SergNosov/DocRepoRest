package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.facade.FileEntityServiceFacade;
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
    private final FileEntityServiceFacade fileEntityServiceFacade;

    @Autowired
    public FileController(FileEntityServiceFacade fileEntityServiceFacade) {
        this.fileEntityServiceFacade = fileEntityServiceFacade;
    }

    @PostMapping("/{docId}")
    public FileEntityDto uploadFile(@PathVariable int docId, @RequestParam("file") MultipartFile file) {

        FileEntity fileEntity = FileEntity.getInstance(file, docId);
        return fileEntityServiceFacade.save(fileEntity);
    }

    @GetMapping("/{id}")
    public FileEntityDto getFileEntity(@PathVariable int id) {
        return fileEntityServiceFacade.findById(id);
    }

    @DeleteMapping("/{id}")
    public CommonMessage deleteFileEntity(@PathVariable int id) {

        int deletedId = fileEntityServiceFacade.deleteById(id);
        return new CommonMessage("Удален файл id - " + deletedId);
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable int id) {
/*
        ResponseEntity<Resource> responseEntity = ResponseEntity.noContent().build();
        FileEntity fileEntity = fileEntityService.findById(id);

        if (fileEntity.getFileByte() != null) {
            Resource resource = new ByteArrayResource(fileEntity.getFileByte());
            String contentType = fileEntity.getContentType();

            responseEntity = ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    fileEntity.getFilename() + "\"")
                    .body(resource);
        }
        return responseEntity;

 */
        return null;
    }
}
