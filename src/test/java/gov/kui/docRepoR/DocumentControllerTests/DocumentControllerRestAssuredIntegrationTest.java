package gov.kui.docRepoR.DocumentControllerTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.Entity.Document;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
    @DisplayName("check HttpStatus of response")
    public void testAddDocumentWithDifferentJsonDocumentValue(JsonDocuments jsonDocumentsEnum) throws IOException {
        Document documentFromJson = mapper.readValue(jsonDocumentsEnum.toString(), Document.class);
        Response response = this.addNewDocument(documentFromJson);

        try {
            int idDoc = response.as(Document.class).getId();
            idDocSet.add(idDoc);
        } catch (Exception rte){}

        int httpStatus = setHttpStatus(jsonDocumentsEnum);
        this.checkStatusCodeAndJSON(response, httpStatus);
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
        return response;
    }

    private void checkStatusCodeAndJSON(Response response, int statusCode) {
        response.then().log().ifValidationFails()
                .statusCode(statusCode)
                .contentType(ContentType.JSON);
    }
}
