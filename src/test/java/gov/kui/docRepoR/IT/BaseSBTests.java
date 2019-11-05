package gov.kui.docRepoR.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.kui.docRepoR.Entity.CommonMessage;
import gov.kui.docRepoR.Entity.DocRepoEntity;
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

    protected BaseSBTests(TestRestTemplate testRestTemplate){
        this.restTemplate = testRestTemplate;
    }

    protected void getAll() {
        ResponseEntity response = restTemplate.exchange(
                entityUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Object>>() {});
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }

    protected ResponseEntity<T> update(DocRepoEntity entity, Class<T> entityClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<T> httpEntity = new HttpEntity(entity, headers);

        ResponseEntity<T> response = restTemplate.exchange(
                entityUrl,
                HttpMethod.PUT,
                httpEntity,
                entityClass);
        return response;
    }

    protected void testUpdateEntityBadId(String entityJson, Class<T> entityClass) throws IOException {
        T entityFromJson = mapper.readValue(entityJson, entityClass);
        T entityExpected = addNewEntity(entityFromJson, entityClass).getBody();
        entityExpected.setId(0);

        ResponseEntity<T> response = update(entityExpected, entityClass);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    protected ResponseEntity<T> addNewEntity(DocRepoEntity entity, Class<T> entityClass) {
        ResponseEntity<T> response = restTemplate.postForEntity(
                entityUrl,
                entity,
                entityClass);

        System.out.println("addNewDocRepoEntity - http code:" + response.getStatusCode() +
                ", entity id: " + response.getBody().getId());
        if (response.getStatusCode() == HttpStatus.OK) {
            int id = response.getBody().getId();
            idEntitySet.add(id);
        }
        return response;
    }

    protected void testGetEntityByIdBad(Class<T> entityClass){
        int badId = Integer.MIN_VALUE;
        ResponseEntity<T> response = getById(badId, entityClass);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    protected ResponseEntity<T> getById(int id, Class<T> entityClass) {
        //restTemplate.getForObject(DocRepoURL.DOCUMENTS.toString() + "/" + id, Document.class);
        ResponseEntity<T> response = restTemplate.exchange(
                entityUrl + "/{id}",
                HttpMethod.GET,
                null,
                entityClass,
                id);
        return response;
    }

    protected void testDeleteEntityByIdBad(){
        int badId = Integer.MIN_VALUE;
        ResponseEntity<CommonMessage> response = deleteById(badId);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
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
