package gov.kui.docRepoR.IT.DocumentControllerIT;

import gov.kui.docRepoR.IT.BaseTests;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.Entity.CommonMessage;
import gov.kui.docRepoR.Entity.Document;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentControllerRestAssuredIntegrationTest extends BaseTests<Document> {

    @BeforeEach
    public void init() {
        requestSpec = RestAssured.given().baseUri(DocRepoURL.DOCUMENTS_LOCALHOST.toString()).contentType(ContentType.JSON);
    }

    @AfterEach
    public void destroy() {
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
        Response response = addNewDocRepoEntity(documentFromJson, Document.class);

        int httpStatus = setHttpStatus(jsonDocumentsEnum);
        checkStatusCodeAndJSON(response, httpStatus);
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(value = JsonDocuments.class, names = {"JSON_GOOD", "JSON_GOOD_2_SENDERS"})
    @DisplayName("2. Add Document. Check Document from response")
    @Order(2)
    public void testAddDocumentOK(JsonDocuments jsonDocumentsEnum) throws IOException {
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        Response response = addNewDocRepoEntity(documentFromJson, Document.class);
        Document documentFromResponse = response.as(Document.class);

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
        Response response = getAll();
        List<Document> documentList = response.as(ArrayList.class);

        checkStatusCodeAndJSON(response, HttpStatus.OK.value());
        assertNotNull(documentList);
    }

    @Test
    @DisplayName("4. Testing the receipt of document by id. OK.")
    @Order(4)
    public void testGetDocumentByIdOk() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson, Document.class).as(Document.class);
        Response response = getById(documentExpected.getId());
        Document documentActual = response.as(Document.class);

        assertAll(
                () -> assertNotNull(documentActual),
                () -> assertEquals(documentExpected.getId(), documentActual.getId()),
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
        int badId = Integer.MIN_VALUE;
        Response response = getById(badId);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("6. Testing delete document by id. OK.")
    @Order(6)
    public void testDeleteDocumentByIdOK() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson, Document.class).as(Document.class);

        Response response = deleteById(documentExpected.getId());
        checkStatusCodeAndJSON(response, HttpStatus.OK.value());
    }

    @Test
    @DisplayName("7. Testing delete document by id. BAD.")
    @Order(7)
    public void testDeleteDocumentByIdBAD() {
        int badId = Integer.MIN_VALUE;
        Response response = deleteById(badId);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("8. Testing update document. OK.")
    @Order(8)
    public void testUpdateDocumentOK() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson, Document.class).as(Document.class);
        documentExpected.setNumber("new123");
        documentExpected.setContent("new content");
        documentExpected.setDocDate(LocalDate.now());

        Response response = update(documentExpected);
        Document documentUpdated = response.as(Document.class);

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
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson, Document.class).as(Document.class);
        documentExpected.setId(0);

        Response response = update(documentExpected);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());

        CommonMessage cm = response.as(CommonMessage.class);
        System.err.println(cm);
    }

    @Test
    @DisplayName("10. Testing update document. Invalid Document.")
    @Order(10)
    public void testUpdateDocumentNotValidDocument() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson, Document.class).as(Document.class);
        documentExpected.setDocDate(null);
        documentExpected.setTitle(" ");
        documentExpected.setDoctype(null);

        Response response = update(documentExpected);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
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
