package gov.kui.docRepoR.DocumentControllerTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.Entity.Document;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class DocumentControllerRestAssuredIntegrationTest {

    private RequestSpecification requestSpec;
    private final String ROOT = "http://localhost:8080/api/documents";
    private  ObjectMapper mapper;
    private Document fromJsonDcument;

    private final String jsonGood = "{ " +
            "\"id\": 1," +
            "\"number\": \"123456789\"," +
            "\"docDate\": \"2019-01-01\"," +
            "\"title\": \"Заголовок\"," +
            "\"content\": \"qwee\"," +
            "\"doctype\": {" +
                "\"id\": 6, " +
                "\"title\": \"Анонимка\"" +
            "}," +
            "\"senders\": [ " +
            "{" +
                "\"id\": 2, " +
                "\"title\": \"ООО \\\"Рога и Копыта\\\"\"" +
            " }" +
            "]" +
            "}";

    @Before
    public void setUp(){
        this.requestSpec = RestAssured.given().baseUri(ROOT).contentType(ContentType.JSON);
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    public void testAddNewDocument() throws IOException {
        fromJsonDcument = mapper.readValue(this.jsonGood, Document.class);
        Response response = this.addNewDocument(fromJsonDcument);
        fromJsonDcument = response.as(Document.class);
        this.checkStatusCodeAndJSON(response, HttpStatus.OK.value());
    }

    @After
    public void tearDone(){
        if (this.fromJsonDcument != null) {
          this.deleteDocument(this.fromJsonDcument.getId());
        }
    }

    private Response deleteDocument(int id) {
        Response response = requestSpec.given()
                .pathParam("id", id)
                .delete("/{id}");
        return response;
    }

    private Response addNewDocument(Document document) {
        return requestSpec.body(document)
                .then().log().body()
                .given().when()
                .post();
    }

    private void checkStatusCodeAndJSON(Response response, int statusCode) {
        response.then().log().ifValidationFails()
                .statusCode(statusCode)
                .contentType(ContentType.JSON);
    }
}
