package gov.kui.docRepoR.DocumentControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.Entity.DocRepoEntity;
import gov.kui.docRepoR.Entity.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentControllerSpringBootTest {

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private Set<Document> entitySet = new HashSet<>();
    private Set<Integer> idEntitySet = new HashSet<>();
    private Document validDocument;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() throws IOException {
        validDocument = mapper.readValue(JsonDocuments.JSON_GOOD_2_SENDERS.toString(), Document.class);
    }

    @AfterEach
    public void tearDown() {
        if (!idEntitySet.isEmpty()) {
            idEntitySet.stream().forEach(id -> this.deleteById(id));
            idEntitySet.clear();
        }
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(JsonDocuments.class)
    @DisplayName("Add Document. Check HttpStatus of response")
    @Order(1)
    public void testAddDocumentWithDifferentJsonDocumentValue(JsonDocuments jsonDocumentsEnum) throws IOException {
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        ResponseEntity<Document> response = addNewDocRepoEntity(documentFromJson);

        System.out.println("Document from response:"+ response.getBody());
        int httpStatus = setHttpStatus(jsonDocumentsEnum);
        assertEquals(httpStatus,response.getStatusCode().value());
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(value = JsonDocuments.class, names = {"JSON_GOOD", "JSON_GOOD_2_SENDERS"})
    @DisplayName("Add Document. Check Document from response")
    @Order(2)
    public void testAddDocumentOK(JsonDocuments jsonDocumentsEnum) throws IOException {
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        ResponseEntity<Document> response = addNewDocRepoEntity(documentFromJson);
        Document documentFromResponse = response.getBody();
        System.out.println("Document from response:" +"\n"+ documentFromResponse);
        assertAll(
                () -> assertEquals(documentFromJson.getNumber(), documentFromResponse.getNumber()),
                () -> assertEquals(documentFromJson.getDocDate(), documentFromResponse.getDocDate()),
                () -> assertEquals(documentFromJson.getTitle(), documentFromResponse.getTitle()),
                () -> assertEquals(documentFromJson.getContent(), documentFromResponse.getContent()),
                () -> assertEquals(documentFromJson.getDoctype(), documentFromResponse.getDoctype()),
                () -> assertEquals(documentFromJson.getSenders().size(), documentFromResponse.getSenders().size()),
                () -> assertArrayEquals(documentFromJson.getSenders().toArray(), documentFromResponse.getSenders().toArray())
        );
    }

    @Test
    @DisplayName("Testing the receipt of all documents")
    @Order(3)
    public void testGetAllDocuments() {
        ResponseEntity<List<Document>> response = getAll();
        List<Document> documentList = response.getBody();
        assertNotNull(documentList);
        assertEquals(HttpStatus.OK.value(),response.getStatusCode().value());
    }

    private ResponseEntity<List<Document>> getAll() {
        ResponseEntity<List<Document>> response = restTemplate.exchange(DocRepoURL.DOCUMENTS.toString(), HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Document>>() {
                });
        return  response;
    }


    @Test
    public void testGetDocumentById() {
        ResponseEntity<Document> response = addNewDocRepoEntity(validDocument);
        Document documentExpected = response.getBody();

        Document documentActual = restTemplate.getForObject(DocRepoURL.DOCUMENTS.toString()
                + "/" + documentExpected.getId(), Document.class);
        System.out.println(documentActual);

        assertAll(
                () -> assertEquals(documentExpected.getNumber(), documentActual.getNumber()),
                () -> assertEquals(documentExpected.getDocDate(), documentActual.getDocDate()),
                () -> assertEquals(documentExpected.getTitle(), documentActual.getTitle()),
                () -> assertEquals(documentExpected.getContent(), documentActual.getContent()),
                () -> assertEquals(documentExpected.getDoctype(), documentActual.getDoctype()),
                () -> assertEquals(documentExpected.getSenders().size(), documentActual.getSenders().size()),
                () -> assertArrayEquals(documentExpected.getSenders().toArray(), documentActual.getSenders().toArray())
        );
    }

    private ResponseEntity<Document> addNewDocRepoEntity(DocRepoEntity entity) {
        ResponseEntity<Document> response = restTemplate.postForEntity(DocRepoURL.DOCUMENTS.toString(),
                entity, Document.class);
        System.out.println("addNewDocRepoEntity - http code:"+response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK) {
            int id = response.getBody().getId();
            idEntitySet.add(id);
        }
        return response;
    }

    private void deleteEntity(DocRepoEntity entity) {
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<Document> httpEntity= new HttpEntity(entity, headers);

        ResponseEntity<Document> response = restTemplate.exchange(DocRepoURL.DOCUMENTS.toString()+"/{id}",
                HttpMethod.DELETE,
                httpEntity,
                Document.class,
                entity.getId());
    }

    private void deleteById(int id){
        restTemplate.delete(DocRepoURL.DOCUMENTS.toString()+"/"+id);
        System.err.println("Удален entity id: "+id);
    }

    private int setHttpStatus(JsonDocuments jsonDocumentsEnum) {
        switch (jsonDocumentsEnum) {
            case JSON_GOOD:
            case JSON_GOOD_2_SENDERS: {
                return HttpStatus.OK.value();
            }
            default: {
                return HttpStatus.BAD_REQUEST.value();
            }
        }
    }
}
