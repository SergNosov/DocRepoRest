package gov.kui.docRepoR.IT.UploadFiles;


import gov.kui.docRepoR.model.UploadFileResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.core.io.Resource;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class UploadFilesIT {
    private final String ROOT = "http://localhost:8080/api/upload";
    private RestTemplate template;
    private HttpHeaders headers;

    public UploadFilesIT() {
        this.template = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    @Test
    public void testUploadFile() throws IOException {
        String pathFile = "e://uploadFile.pdf";
        File file = new File(pathFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files", getTestFile());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = template.postForEntity(ROOT, requestEntity, String.class);

    }

    public Resource getTestFile() throws IOException {
        Path testFile = new File("e://uploadFile.pdf").toPath();
        return new FileSystemResource(testFile);
    }
}
