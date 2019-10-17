package gov.kui.docRepoR;

import gov.kui.docRepoR.Entity.CommonMessage;
import gov.kui.docRepoR.Entity.Doctype;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.List;


public class DoctypeControllerRestAssuredIntegrationTests {
    private final RequestSpecification requestSpec;
    private final String ROOT = "http://localhost:8080/api/doctypes";

    public DoctypeControllerRestAssuredIntegrationTests() {
        this.requestSpec = RestAssured.given().baseUri(ROOT).contentType(ContentType.JSON);
    }

    @Test
    public void checkGetAllDoctypes() {
        Response response = this.getAll();
        this.checkStatusCodeAndJSON(response, HttpStatus.OK.value());

        JsonPath jsonPath = response.then().extract().jsonPath();

        List<Doctype> returnedDoctypes = jsonPath.getList("", Doctype.class);
        Assert.assertNotNull(returnedDoctypes);
        Assert.assertFalse(returnedDoctypes.isEmpty());
    }

    @Test
    public void checkDoctypeController_Ok() {
        Response response = this.addNewDoctype();
        Doctype newDoctype = response.as(Doctype.class);
        Assert.assertNotNull(newDoctype);
        Assert.assertNotEquals(newDoctype.getId(), 0);

        final int id = newDoctype.getId();

        Doctype doctypeById = this.getDoctypeById(id).as(Doctype.class);
        Assert.assertEquals(doctypeById.getId(), newDoctype.getId());

        CommonMessage commonMessage = this.delDoctypeById(id).as(CommonMessage.class);
        Assert.assertEquals(commonMessage.getMessage(), "Удален тип документа id - " + id);

        response = this.getDoctypeById(id);
        this.checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void checkGetById_BAD() {
        int id = Integer.MAX_VALUE;
        Response response = this.getDoctypeById(id);
        this.checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void checkDeleteById_OK() {
        Doctype newDoctype = this.addNewDoctype(this.createRandomDoctype()).as(Doctype.class);
        Response response = this.delDoctypeById(newDoctype.getId());
        this.checkStatusCodeAndJSON(response, HttpStatus.OK.value());
    }

    @Test
    public void checkDeleteById_BAD() {
        int id = Integer.MIN_VALUE;
        Response response = this.delDoctypeById(id);
        this.checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void checkUpdateDoctype_OK() {
        Doctype newDoctype = this.addNewDoctype(this.createRandomDoctype()).as(Doctype.class);
        newDoctype.setTitle(newDoctype.getTitle() + "1");

        Response response = this.updateDoctype(newDoctype);
        this.checkStatusCodeAndJSON(response, HttpStatus.OK.value());

        CommonMessage commonMessage = this.delDoctypeById(newDoctype.getId()).as(CommonMessage.class);
        Assert.assertEquals(commonMessage.getMessage(), "Удален тип документа id - " + newDoctype.getId());
    }

    @Test
    public void checkUpdateDoctype_BAD() {
        Doctype newDoctype = this.addNewDoctype(this.createRandomDoctype())
                .as(Doctype.class);
        Response response = this.updateDoctype(newDoctype);
        this.checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());

        CommonMessage commonMessage = this.delDoctypeById(newDoctype.getId()).as(CommonMessage.class);
        Assert.assertEquals(commonMessage.getMessage(), "Удален тип документа id - " + newDoctype.getId());
    }

    @Test
    public void checkAddNotUniqueValue() {
        Doctype newDoctype = this.createRandomDoctype();
        newDoctype = this.addNewDoctype(newDoctype).as(Doctype.class);

        Response response = this.addNewDoctype(newDoctype);
        this.checkStatusCodeAndJSON(response, HttpStatus.BAD_REQUEST.value());

        CommonMessage commonMessage = this.delDoctypeById(newDoctype.getId()).as(CommonMessage.class);
        Assert.assertEquals(commonMessage.getMessage(), "Удален тип документа id - " + newDoctype.getId());
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

    private void checkStatusCodeAndJSON(Response response, int statusCode) {
        response.then().log().ifValidationFails()
                .statusCode(statusCode)
                .contentType(ContentType.JSON);
    }

    private Response getAll() {
        return requestSpec.get();
    }
}
