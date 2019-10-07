package gov.kui.docRepoR;

import gov.kui.docRepoR.Entity.Doctype;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


public class DoctypeControllerRestAssuredIntegrationTests {
    private final RequestSpecification requestSpec;
    private final String ROOT = "http://localhost:8080/api/doctypes";

    public DoctypeControllerRestAssuredIntegrationTests(){
        this.requestSpec = RestAssured.given().baseUri(ROOT);
    }
    @Test
    public void checkGetAllDoctypes() {
        /*
        RestAssured.when().get(ROOT)
                .then().log().body()
                .statusCode(HttpStatus.OK.value());

         */
        Response response = requestSpec.get();

        response.then().statusCode(HttpStatus.OK.value())
                .assertThat().contentType(ContentType.JSON);

       // List<Doctype> returnedDoctypes = Arrays.asList(response.getBody().as(Doctype[].class));
        JsonPath jsonPath = response.then().extract().jsonPath();
        List<Doctype> returnedDoctypes = jsonPath.getList("", Doctype.class);
        assertNotNull(returnedDoctypes);
        assertFalse(returnedDoctypes.isEmpty());
    }
}
