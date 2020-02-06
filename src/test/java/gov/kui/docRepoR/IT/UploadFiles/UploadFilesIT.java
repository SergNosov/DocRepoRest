package gov.kui.docRepoR.IT.UploadFiles;


import gov.kui.docRepoR.domain.AuthToken;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.security.TokenAuthentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UploadFilesIT {
    private final String ROOT = "http://localhost:8080/api/files/";
    private final String DOCROOT = "http://localhost:8080/api/documents/files/";
    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    private AuthToken token;

    @BeforeEach
    public void init() {
        token = TokenAuthentification.getAuthToken();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(token.getToken());
    }

    @Test
    public void testUploadFileOk() {
        int documentId = 21;

        ResponseEntity<FileEntity> responseEntity = restTemplate.postForEntity(
                ROOT + documentId,
                generateEntity(),
                FileEntity.class
        );

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        FileEntity fileEntity = responseEntity.getBody();
        assertEquals(documentId, fileEntity.getDocumentId());
    }

    @Test
    public void getAllFileEntity() {
        int documentId = 21;

        this.headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ResponseEntity<List<FileEntity>> responseEntity = restTemplate.exchange(
                DOCROOT + documentId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<FileEntity>>() {
                }
        );
    }

    private HttpEntity<MultiValueMap<String, Object>> generateEntity() {
        Path testFile = new File("d://54-45_4.tif").toPath();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(testFile));
     //   body.add("file", null);

        return new HttpEntity<>(body, headers);
    }
}
