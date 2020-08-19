package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.FileEntityBlob;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.facade.FileEntityServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileEntityServiceFacade fileEntityServiceFacade;

    @Autowired
    public FileController(FileEntityServiceFacade fileEntityServiceFacade) {
        this.fileEntityServiceFacade = fileEntityServiceFacade;
    }

    @PostMapping("/{docId}")
    public FileEntityDto uploadFile(@PathVariable int docId, @RequestParam("file") MultipartFile file) {

        FileEntityBlob fileEntityBlob = FileEntityBlob.getInstance(file, docId);
        return fileEntityServiceFacade.save(fileEntityBlob);
    }

    @GetMapping("/{id}")
    public FileEntityDto getFileEntity(@PathVariable int id) {
        return fileEntityServiceFacade.findDtoById(id);
    }

    @DeleteMapping("/{id}")
    public CommonMessage deleteFileEntity(@PathVariable int id) {

        int deletedId = fileEntityServiceFacade.deleteById(id);
        return new CommonMessage("Удален файл id - " + deletedId);
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable int id) {

        ResponseEntity<Resource> responseEntity = fileEntityServiceFacade.getResourseById(id);
        return responseEntity;
    }
}
