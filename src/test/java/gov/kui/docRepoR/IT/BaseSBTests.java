package gov.kui.docRepoR.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.kui.docRepoR.model.CommonMessage;
import gov.kui.docRepoR.model.DocRepoEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class BaseSBTests <T extends DocRepoEntity>{
    protected ObjectMapper mapper;
    protected Set<Integer> idEntitySet;
    protected TestRestTemplate restTemplate;
    protected String entityUrl;
    protected Class<T> entityClass;

    protected BaseSBTests(TestRestTemplate testRestTemplate, Class<T> entityClass){
        this.restTemplate = testRestTemplate;
        this.entityClass = entityClass;
    }

    protected void testAddEntityWithDifferentJsonValue(String entityJson, int httpStatus) throws IOException {
        T entityFromJson = mapper.readValue(entityJson, this.entityClass);
        ResponseEntity<T> response = addNewEntity(entityFromJson);

        System.out.println("Entity("+this.entityClass.getName()+") from response:" + response.getBody());
        assertEquals(httpStatus, response.getStatusCode().value());
    }

    protected void testUpdateEntityBadId(String entityJson) throws IOException {
        T entityFromJson = mapper.readValue(entityJson, entityClass);
        T entityExpected = addNewEntity(entityFromJson).getBody();
        entityExpected.setId(0);

        ResponseEntity<T> response = update(entityExpected);
        System.out.println("testUpdateEntityBadId - http code: :"+response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    protected void testGetEntityByIdBad(){
        int badId = Integer.MIN_VALUE;
        ResponseEntity<T> response = getById(badId);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    protected void testDeleteEntityByIdBad(){
        int badId = Integer.MIN_VALUE;
        ResponseEntity<CommonMessage> response = deleteById(badId);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    protected ResponseEntity<List<T>> getAll() {
        ResponseEntity<List<T>> response = restTemplate.exchange(
                entityUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<T>>() {});
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        return response;
    }

    protected ResponseEntity<T> update(DocRepoEntity entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<T> httpEntity = new HttpEntity(entity, headers);

        ResponseEntity<T> response = restTemplate.exchange(
                entityUrl,
                HttpMethod.PUT,
                httpEntity,
                this.entityClass);
        return response;
    }

    protected ResponseEntity<T> addNewEntity(DocRepoEntity entity) {
        ResponseEntity<T> response = restTemplate.postForEntity(
                entityUrl,
                entity,
                this.entityClass);

        System.out.println("addNewDocRepoEntity - http code:" + response.getStatusCode() +
                ", entity id: " + response.getBody().getId());
        if (response.getStatusCode() == HttpStatus.OK) {
            int id = response.getBody().getId();
            idEntitySet.add(id);
        }
        return response;
    }

    protected ResponseEntity<T> getById(int id) {
        //restTemplate.getForObject(DocRepoURL.DOCUMENTS.toString() + "/" + id, Document.class);
        ResponseEntity<T> response = restTemplate.exchange(
                entityUrl + "/{id}",
                HttpMethod.GET,
                null,
                this.entityClass,
                id);
        return response;
    }

    protected ResponseEntity<CommonMessage> deleteById(int id) {
        //restTemplate.delete(DocRepoURL.DOCUMENTS.toString() + "/" + id)
        ResponseEntity<CommonMessage> response = restTemplate.exchange(
                entityUrl + "/{id}",
                HttpMethod.DELETE,
                null,
                CommonMessage.class,
                id);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.err.println("Удален entity id: " + id);
        }
        return response;
    }
}
