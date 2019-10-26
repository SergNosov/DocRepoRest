package gov.kui.docRepoR.DocumentControllerTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.Entity.Document;
import gov.kui.docRepoR.Entity.Sender;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentControllerRestAssuredIntegrationTest {
    private static RequestSpecification requestSpec;
    private static ObjectMapper mapper;
    private static Set<Integer> idDocSet = new HashSet<>();

    @BeforeAll
    public static void init(){
        requestSpec = RestAssured.given().baseUri(DocRepoURL.DOCUMENTS.toString()).contentType(ContentType.JSON);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @AfterAll
    public static void destroy(){
        if (!idDocSet.isEmpty()) {
            idDocSet.stream().forEach(id -> deleteDocument(id));
        }
    }

    @ParameterizedTest(name = "{index} json = {0}")
    @EnumSource(JsonDocuments.class)
    @DisplayName("Add Document. Check HttpStatus of response")
    @Order(1)
    public void testAddDocumentWithDifferentJsonDocumentValue(JsonDocuments jsonDocumentsEnum) throws IOException {
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        Response response = addNewDocument(documentFromJson);

        int httpStatus = setHttpStatus(jsonDocumentsEnum);
        checkStatusCodeAndJSON(response, httpStatus);
    }

    @ParameterizedTest
    @EnumSource(value = JsonDocuments.class, names = {"JSON_GOOD"})
    @DisplayName("Add Document. Check Document from response")
    @Order(2)
    public void testAddDocumentOK(JsonDocuments jsonDocumentsEnum) throws IOException{
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        Response response = addNewDocument(documentFromJson);
        Document documentFromResponse = response.as(Document.class);

        assertAll(
                ()-> assertEquals(documentFromJson.getNumber(),documentFromResponse.getNumber()),
                ()-> assertEquals(documentFromJson.getDocDate(),documentFromResponse.getDocDate()),
                ()-> assertEquals(documentFromJson.getTitle(),documentFromResponse.getTitle()),
                ()-> assertEquals(documentFromJson.getContent(),documentFromResponse.getContent()),
                ()-> assertEquals(documentFromJson.getDoctype(),documentFromResponse.getDoctype()),
                ()-> assertEquals(documentFromJson.getSenders().size(),documentFromResponse.getSenders().size()),
                ()-> assertArrayEquals(documentFromJson.getSenders().toArray(),documentFromResponse.getSenders().toArray())
        );
    }

    @Test
    @DisplayName("Testing the receipt of all documents")
    @Order(3)
    public void testGetAllDocuments(){
        Response response = getAll();
        List<Document> documentList = response.as(ArrayList.class);

        checkStatusCodeAndJSON(response,HttpStatus.OK.value());
        assertNotNull(documentList);
    }

    private Response getAll(){
        Response response = requestSpec.then().log().body().given().get();
        return response;
    }

    private int setHttpStatus(JsonDocuments jsonDocumentsEnum) {
        if (jsonDocumentsEnum.name().equals(JsonDocuments.JSON_GOOD.name())){
            return HttpStatus.OK.value();
        } else {
            return HttpStatus.BAD_REQUEST.value();
        }
    }

    private static Response deleteDocument(int id) {
        Response response = requestSpec.given()
                .pathParam("id", id)
                .delete("/{id}");
        return response;
    }

    private Response addNewDocument(Document document) {
        Response response = requestSpec.body(document)
                .then().log().body()
                .given().when()
                .post();

        try {
            int idDoc = response.as(Document.class).getId();
            idDocSet.add(idDoc);
        } catch (Exception rte){}

        return response;
    }

    private void checkStatusCodeAndJSON(Response response, int statusCode) {
        response.then().log().ifValidationFails()
                .statusCode(statusCode)
                .contentType(ContentType.JSON);
    }
}
