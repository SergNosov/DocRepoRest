package gov.kui.docRepoR.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/documents/{id}")
@CrossOrigin(origins = "http://localhost:4201")
@PropertySource("classpath:application.properties")
public class DocumentFilesController {
    private final File uploadDirRoot;

    public DocumentFilesController(@Value("${image.upload.dir}")String uploadDir) {
        this.uploadDirRoot = new File(uploadDir);
    }

    @PostMapping("/upload")
    public String uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile uploadfile){
        System.out.println("------uploadFile: "+uploadfile.getName());
        return "upload Ok";
    }
}
