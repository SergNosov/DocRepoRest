package gov.kui.docRepoR;

import gov.kui.docRepoR.Entity.DocRepoEntity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

import java.util.List;

public abstract class BaseTests<T extends DocRepoEntity> {
    private final Class<T> entityClass;
    private final RequestSpecification requestSpec;

    public BaseTests(String ROOT, Class<T> entityClass) {
        this.requestSpec = RestAssured.given().baseUri(ROOT).contentType(ContentType.JSON);;
        this.entityClass = entityClass;
    }

    protected void checkGetAll(){
        Response response = requestSpec.get();
        this.checkStatusCodeAndJSON(response, HttpStatus.OK.value());

        JsonPath jsonPath = response.then().extract().jsonPath();

        List<T> returnedEntyties = jsonPath.getList("", entityClass);
        Assert.assertNotNull(returnedEntyties);
        Assert.assertFalse(returnedEntyties.isEmpty());
    }

    protected void checkGetById(){
        final int id = Integer.MAX_VALUE;
        Response response = this.getEntityById(id);
        this.checkStatusCodeAndJSON(response,HttpStatus.BAD_REQUEST.value());
    }

    protected void checkAddNewEntity(T entity){
        Response response = this.addNewEntyty(entity);
        this.checkStatusCodeAndJSON(response,HttpStatus.OK.value());

        T newEntity = response.as(entityClass);
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(newEntity.getId(),entity.getId());
    }

    private void checkStatusCodeAndJSON(Response response, int statusCode){
        response.then().log().ifValidationFails()
                .statusCode(statusCode)
                .contentType(ContentType.JSON);
    }

    private Response getEntityById(int id) {
        Response response = requestSpec.given()
                .pathParam("id", id)
                .get("/{id}");
        return response;
    }

    private Response addNewEntyty(T entity) {
        return requestSpec.body(entity)
                .then().log().body()
                .given().when()
                .post();
    }
}
