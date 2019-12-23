package gov.kui.docRepoR.IT.UploadFiles;


import gov.kui.docRepoR.model.AuthToken;
import gov.kui.docRepoR.model.FileEntity;
import gov.kui.docRepoR.security.TokenAuthentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UploadFilesIT {
    private final String ROOT = "http://localhost:8080/api/documents/";
    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    private static AuthToken token;

    @BeforeEach
    public void init() {
        token = TokenAuthentification.getAuthToken();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(token.getToken());
    }

    @Test
    public void testUploadFile() {
        int documentId = 1;

        ResponseEntity<FileEntity> responseEntity = restTemplate.postForEntity(
                ROOT + documentId + "/upload",
                generateEntity(),
                FileEntity.class
        );

        FileEntity fileEntity = responseEntity.getBody();

       // assertEquals(documentId,fileEntity.getDocument().getId());
    }

    private HttpEntity<MultiValueMap<String, Object>> generateEntity() {
        Path testFile = new File("e://uploadFile.pdf").toPath();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(testFile));

        return new HttpEntity<>(body, headers);
    }

    /*
    private Resource getTestFile() throws IOException {
        Path testFile = new File("e://uploadFile.pdf").toPath();
        return new FileSystemResource(testFile);
    }
     */

}
