package gov.kui.docRepoR.DocumentControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.Entity.Document;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentControllerRestAssuredIntegrationTest {
    private RequestSpecification requestSpec;
    private ObjectMapper mapper;
    private Set<Integer> idDocSet = new HashSet<>();

    @BeforeEach
    public void init() {
        requestSpec = RestAssured.given().baseUri(DocRepoURL.DOCUMENTS.toString()).contentType(ContentType.JSON);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @AfterEach
    public void destroy() {
        if (!idDocSet.isEmpty()) {
            idDocSet.stream().forEach(id -> deleteById(id));
            idDocSet.clear();
        }
        System.err.println("Размер idDocSet: " + idDocSet.size());
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(JsonDocuments.class)
    @DisplayName("Add Document. Check HttpStatus of response")
    @Order(1)
    public void testAddDocumentWithDifferentJsonDocumentValue(JsonDocuments jsonDocumentsEnum) throws IOException {
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        Response response = addNewDocRepoEntity(documentFromJson);

        int httpStatus = setHttpStatus(jsonDocumentsEnum);
        checkStatusCodeAndJSON(response, httpStatus);
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(value = JsonDocuments.class, names = {"JSON_GOOD", "JSON_GOOD_2_SENDERS"})
    @DisplayName("Add Document. Check Document from response")
    @Order(2)
    public void testAddDocumentOK(JsonDocuments jsonDocumentsEnum) throws IOException {
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        Response response = addNewDocRepoEntity(documentFromJson);
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
    @DisplayName("Testing the receipt of all documents")
    @Order(3)
    public void testGetAllDocuments() {
        Response response = getAll();
        List<Document> documentList = response.as(ArrayList.class);

        checkStatusCodeAndJSON(response, HttpStatus.OK.value());
        assertNotNull(documentList);
    }

    @Test
    @DisplayName("Testing the receipt of document by id. OK.")
    @Order(4)
    public void testGetDocumentByIdOk() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson).as(Document.class);
        Response response = getById(documentExpected.getId());
        Document documentActual = response.as(Document.class);

        assertAll(
                ()-> assertNotNull(documentActual),
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
    @DisplayName("Testing the receipt of document by id. BAD.")
    @Order(5)
    public void testGetDocumentByIdBAD() {
        int badId = Integer.MIN_VALUE;
        Response response = getById(badId);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Testing delete document by id. OK.")
    @Order(6)
    public void testDeleteDocumentByIdOK() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson).as(Document.class);

        Response response = deleteById(documentExpected.getId());
        checkStatusCodeAndJSON(response, HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Testing delete document by id. BAD.")
    @Order(7)
    public void testDeleteDocumentByIdBAD() {
        int badId = Integer.MIN_VALUE;
        Response response = deleteById(badId);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Testing update document. OK.")
    @Order(8)
    public void testUpdateDocumentOK() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson).as(Document.class);
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
    @DisplayName("Testing update document. Bad ID.")
    @Order(9)
    public void testUpdateDocumentBadID() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson).as(Document.class);
        documentExpected.setId(0);

        Response response = update(documentExpected);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Testing update document. Invalid Document.")
    @Order(10)
    public void testUpdateDocumentNotValidDocument() throws IOException {
        Document documentFromJson = mapper.readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        Document documentExpected = addNewDocRepoEntity(documentFromJson).as(Document.class);
        documentExpected.setDocDate(null);
        documentExpected.setTitle(" ");
        documentExpected.setDoctype(null);

        Response response = update(documentExpected);
        checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    private Response update(Document document) {
        Response response = requestSpec.body(document)
                .then().log().body()
                .given().when()
                .put();
        return response;
    }


    private Response getById(int id) {
        return requestSpec.given()
                .pathParam("id", id)
                .get("/{id}");
    }

    private Response getAll() {
        Response response = requestSpec.then().log().body().given().get();
        return response;
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

    private Response deleteById(int id) {
        Response response = requestSpec.given()
                .pathParam("id", id)
                .delete("/{id}");
        return response;
    }

    private Response addNewDocRepoEntity(Document document) {
        Response response = requestSpec.body(document)
                .then().log().body()
                .given().when()
                .post();

        try {
            int idDoc = response.as(Document.class).getId();
            idDocSet.add(idDoc);
        } catch (Exception rte) {
        }

        return response;
    }

    private void checkStatusCodeAndJSON(Response response, int statusCode) {
        response.then().log().ifValidationFails()
                .statusCode(statusCode)
                .contentType(ContentType.JSON);
    }
}
