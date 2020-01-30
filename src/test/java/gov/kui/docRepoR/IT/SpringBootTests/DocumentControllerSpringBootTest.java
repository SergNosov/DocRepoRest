package gov.kui.docRepoR.IT.SpringBootTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.IT.JsonDocuments;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentControllerSpringBootTest extends BaseSBTests<Document> {

    @Autowired
    public DocumentControllerSpringBootTest(TestRestTemplate testRestTemplate) {
        super(testRestTemplate, Document.class);
    }

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        entityUrl = DocRepoURL.DOCUMENTS.toString();
        idEntitySet = new HashSet<>();
    }

    @AfterEach
    public void tearDown() {
        if (!idEntitySet.isEmpty()) {
            idEntitySet.stream().forEach(id -> deleteById(id));
            idEntitySet.clear();
        }
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(JsonDocuments.class)
    @DisplayName("1. Add Document. Check HttpStatus of response")
    @Order(1)
    public void testAddDocumentWithDifferentJsonDocumentValue(JsonDocuments jsonDocumentsEnum) throws IOException {
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        ResponseEntity<Document> response = addNewEntity(documentFromJson);

        System.out.println("Document from response:" + response.getBody());
        int httpStatus = setHttpStatus(jsonDocumentsEnum);
        assertEquals(httpStatus, response.getStatusCode().value());
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(value = JsonDocuments.class, names = {"JSON_GOOD", "JSON_GOOD_2_SENDERS"})
    @DisplayName("2. Add Document. Check Document from response")
    @Order(2)
    public void testAddDocumentOK(JsonDocuments jsonDocumentsEnum) throws IOException {
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        ResponseEntity<Document> response = addNewEntity(documentFromJson);
        Document documentFromResponse = response.getBody();
        System.out.println("Document from response:" + "\n" + documentFromResponse);
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
    @DisplayName("3. Testing the receipt of all documents")
    @Order(3)
    public void testGetAllDocuments() {
       getAll();
    }

    @Test
    @DisplayName("4. Testing the receipt of document by id. OK.")
    @Order(4)
    public void testGetDocumentById() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD_2_SENDERS.toString(), Document.class);
        ResponseEntity<Document> response = addNewEntity(documentFromJson);
        Document documentExpected = response.getBody();

        ResponseEntity<Document> responseEntity = getById(documentExpected.getId());
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());

        Document documentActual = responseEntity.getBody();
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

    @Test
    @DisplayName("5. Testing the receipt of document by id. BAD.")
    @Order(5)
    public void testGetDocumentByIdBAD() {
        testGetEntityByIdBad();
    }

    @Test
    @DisplayName("6. Testing delete document by id. OK.")
    @Order(6)
    public void testDeleteDocumentByIdOK() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewEntity(documentFromJson).getBody();

        ResponseEntity<CommonMessage> response = deleteById(documentExpected.getId());
        System.out.println("response: " + response.getBody().getMessage());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals("Удален документ id - " + documentExpected.getId(),
                        response.getBody().getMessage())
        );
    }

    @Test
    @DisplayName("7. Testing delete document by id. BAD.")
    @Order(7)
    public void testDeleteDocumentByIdBAD() {
        testDeleteEntityByIdBad();
    }

    @Test
    @DisplayName("8. Testing update document. OK.")
    @Order(8)
    public void testUpdateDocumentOK() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewEntity(documentFromJson).getBody();
        documentExpected.setNumber("new123");
        documentExpected.setContent("new content");
        documentExpected.setDocDate(LocalDate.now());

        ResponseEntity<Document> response = update(documentExpected);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Document documentUpdated = response.getBody();
        assertAll(
                () -> assertNotNull(documentUpdated),
                () -> assertEquals(documentExpected.getId(), documentUpdated.getId()),
                () -> assertEquals(documentExpected.getNumber(), documentUpdated.getNumber()),
                () -> assertEquals(documentExpected.getDocDate(), documentUpdated.getDocDate()),
                () -> assertEquals(documentExpected.getTitle(), documentUpdated.getTitle()),
                () -> assertEquals(documentExpected.getContent(), documentUpdated.getContent()),
                () -> assertEquals(documentExpected.getDoctype(), documentUpdated.getDoctype()),
                () -> assertEquals(documentExpected.getSenders().size(), documentUpdated.getSenders().size()),
                () -> assertArrayEquals(documentExpected.getSenders().toArray(), documentUpdated.getSenders().toArray())
        );
    }

    @Test
    @DisplayName("9. Testing update document. Bad ID.")
    @Order(9)
    public void testUpdateDocumentBadID() throws IOException {
        testUpdateEntityBadId(JsonDocuments.JSON_GOOD.toString());
    }

    @Test
    @DisplayName("10. Testing update document. Invalid Document.")
    @Order(10)
    public void testUpdateDocumentNotValidDocument() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewEntity(documentFromJson).getBody();
        documentExpected.setDocDate(null);
        documentExpected.setTitle(" ");
        documentExpected.setDoctype(null);

        ResponseEntity<Document> response = update(documentExpected);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    private int setHttpStatus(JsonDocuments jsonDocumentsEnum) {
        switch (jsonDocumentsEnum) {
            case JSON_GOOD:
            case JSON_ZERO_ID:
            case JSON_GOOD_2_SENDERS: {
                return HttpStatus.OK.value();
            }
            default: {
                return HttpStatus.BAD_REQUEST.value();
            }
        }
    }
}
