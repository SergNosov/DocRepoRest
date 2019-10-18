package gov.kui.docRepoR.DocumentControllerTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.Entity.Document;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RunWith(value = Parameterized.class)
public class DocumentControllerRestAssuredIntegrationTest {

    private static RequestSpecification requestSpec;
    private static ObjectMapper mapper;
    private static Set<Integer> idDocSet = new HashSet<>();

    private String jsonDocumentValue;
    private int httpStatus;

    public DocumentControllerRestAssuredIntegrationTest(String jsonDocumentValue, int httpStatus) {
        this.jsonDocumentValue = jsonDocumentValue;
        this.httpStatus = httpStatus;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters(){
        return Arrays.asList( new Object[][]{
                {JsonDocuments.JSON_GOOD.toString(), HttpStatus.OK.value()},
                {JsonDocuments.JSON_NULL.toString(), HttpStatus.BAD_REQUEST.value()},
                {JsonDocuments.JSON_NO_REQURED_FIELDS.toString(), HttpStatus.BAD_REQUEST.value()}
        });
    }

    @BeforeClass
    public static void setUp(){
        requestSpec = RestAssured.given().baseUri(DocRepoURL.DOCUMENTS_URL.toString()).contentType(ContentType.JSON);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    public void testAddDocumentWithDifferentJsonDocumentValue() throws IOException {
        Document fromJsonDocument = mapper.readValue(jsonDocumentValue, Document.class);
        Response response = this.addNewDocument(fromJsonDocument);
        try {
            int idDoc = response.as(Document.class).getId();
            idDocSet.add(idDoc);
        } catch (Exception rte){
        }
        this.checkStatusCodeAndJSON(response, this.httpStatus);
    }

    @AfterClass
    public static void tearDone(){
        if (!idDocSet.isEmpty()) {
            idDocSet.stream().forEach(id -> deleteDocument(id));
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
