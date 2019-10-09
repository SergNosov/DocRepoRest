package gov.kui.docRepoR;

import gov.kui.docRepoR.Entity.CommonResponse;
import gov.kui.docRepoR.Entity.Doctype;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import javax.print.Doc;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


public class DoctypeControllerRestAssuredIntegrationTests {
    private final RequestSpecification requestSpec;
    private final String ROOT = "http://localhost:8080/api/doctypes";

    public DoctypeControllerRestAssuredIntegrationTests() {
        this.requestSpec = RestAssured.given().baseUri(ROOT).contentType("application/json");
    }

    @Test
    public void checkGetAllDoctypes() {
        Response response = requestSpec.get();

        response.then().assertThat().statusCode(HttpStatus.OK.value());
        response.then().assertThat().contentType(ContentType.JSON);

        JsonPath jsonPath = response.then().extract().jsonPath();

        List<Doctype> returnedDoctypes = jsonPath.getList("", Doctype.class);
        assertNotNull(returnedDoctypes);
        assertFalse(returnedDoctypes.isEmpty());
    }

    @Test
    public void checkDoctypeController_Ok() {
        Response response = this.addNewDoctype();
        Doctype newDoctype = response.as(Doctype.class);
        assertNotNull(newDoctype);

        final int id = newDoctype.getId();

        Doctype doctypeById = this.getDoctypeById(id).as(Doctype.class);
        assertEquals(doctypeById.getId(), newDoctype.getId());

        CommonResponse commonResponse = this.delDoctypeById(id).as(CommonResponse.class);
        assertEquals(commonResponse.getMessage(), "Удален отправитель id - " + id);

        response = this.getDoctypeById(id);
        response.then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void checkGetById_BAD() {
        int id = Integer.MAX_VALUE;
        Response response = this.getDoctypeById(id);
        response.then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void checkDeleteById_OK(){
        Doctype newDoctype = this.addNewDoctype(this.createRandomDoctype()).as(Doctype.class);
        Response response = this.delDoctypeById(newDoctype.getId());
        response.then().assertThat().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void checkDeleteById_BAD() {
        int id = Integer.MIN_VALUE;
        Response response = this.delDoctypeById(id);
        response.then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void checkUpdateDoctype_OK() {
        Doctype newDoctype = this.addNewDoctype(this.createRandomDoctype()).as(Doctype.class);
        newDoctype.setTitle(newDoctype.getTitle()+"1");

        Response response = this.updateDoctype(newDoctype);
        response.then().assertThat().statusCode(HttpStatus.OK.value());

        CommonResponse commonResponse = this.delDoctypeById(newDoctype.getId()).as(CommonResponse.class);
        assertEquals(commonResponse.getMessage(), "Удален отправитель id - " + newDoctype.getId());
    }

    @Test
    public void checkUpdateDoctype_BAD() {
        Doctype newDoctype = this.addNewDoctype(this.createRandomDoctype())
                .as(Doctype.class);
        Response response = this.updateDoctype(newDoctype);
        response.then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());

        CommonResponse commonResponse = this.delDoctypeById(newDoctype.getId()).as(CommonResponse.class);
        assertEquals(commonResponse.getMessage(), "Удален отправитель id - " + newDoctype.getId());
    }
    @Test
    public void checkAddNotUniqueValue() {
        Doctype newDoctype = this.createRandomDoctype();
        newDoctype = this.addNewDoctype(newDoctype).as(Doctype.class);

        Response response = this.addNewDoctype(newDoctype);
        response.then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());

        CommonResponse commonResponse = this.delDoctypeById(newDoctype.getId()).as(CommonResponse.class);
        assertEquals(commonResponse.getMessage(), "Удален отправитель id - " + newDoctype.getId());
    }

    private Response addNewDoctype(Doctype doctype) {
        return requestSpec.body(doctype)
                .then().log().body()
                .given().when()
                .post();
    }

    private Response addNewDoctype() {
        Doctype newDoctype = this.createRandomDoctype();
        Response response = this.addNewDoctype(newDoctype);

        response.then().assertThat().statusCode(HttpStatus.OK.value());
        return response;
    }

    private Response getDoctypeById(int id) {
        Response response = requestSpec.given()
                .pathParam("id", id)
                .get("/{id}");
        return response;
    }

    private Response delDoctypeById(int id) {
        Response response = requestSpec.given()
                .pathParam("id", id)
                .delete("/{id}");
        return response;
    }

    private Response updateDoctype(Doctype doctype) {
        Response response = requestSpec.body(doctype)
                .then().log().body()
                .given().when()
                .put();

        return response;
    }

    private Doctype createRandomDoctype() {
        String doctypeTitle = RandomStringUtils.randomAlphanumeric(5);
        return new Doctype(doctypeTitle);
    }
}
