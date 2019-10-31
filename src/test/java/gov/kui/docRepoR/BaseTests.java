package gov.kui.docRepoR;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.kui.docRepoR.Entity.DocRepoEntity;
import gov.kui.docRepoR.Entity.Sender;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseTests<T extends DocRepoEntity> {
    protected RequestSpecification requestSpec;
    protected ObjectMapper mapper;
    protected Set<Integer> idEntitySet = new HashSet<>();

    protected Response update(DocRepoEntity entity) {
        Response response = requestSpec.body(entity)
                .then().log().body()
                .given().when()
                .put();
        return response;
    }

    protected Response getById(int id) {
        return requestSpec.given()
                .pathParam("id", id)
                .get("/{id}");
    }

    protected Response getAll() {
        Response response = requestSpec.then().log().body().given().get();
        return response;
    }

    protected Response deleteById(int id) {
        Response response = requestSpec.given()
                .pathParam("id", id)
                .delete("/{id}");
        return response;
    }

    protected Response addNewDocRepoEntity(DocRepoEntity entity, Class<T> entityClass) {
        Response response = requestSpec.body(entity)
                .then().log().body()
                .given().when()
                .post();

        if (response.getStatusCode() == HttpStatus.OK.value()) {
            int id = response.as(entityClass).getId();
            idEntitySet.add(id);
        }
        return response;
    }

    protected void checkStatusCodeAndJSON(Response response, int statusCode) {
        response.then().log().ifValidationFails()
                .statusCode(statusCode)
                .contentType(ContentType.JSON);
    }
}
