package gov.kui.docRepoR.IT.SpringBootTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.BaseEntity;
import gov.kui.docRepoR.security.TokenAuthentification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
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

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class BaseSBTests<T extends BaseEntity> {
    protected ObjectMapper mapper;
    protected Set<Integer> idEntitySet;
    protected TestRestTemplate restTemplate;
    protected String entityUrl;
    protected Class<T> entityClass;
    protected HttpHeaders httpHeaders = new HttpHeaders();

    protected BaseSBTests(TestRestTemplate testRestTemplate, Class<T> entityClass) {
        this.restTemplate = testRestTemplate;
        this.entityClass = entityClass;

        initHttpHeaders();
    }

    private void initHttpHeaders() {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.setBearerAuth(TokenAuthentification.getToken());
    }

    protected void testAddEntityWithDifferentJsonValue(String entityJson, int httpStatus) throws IOException {
        T entityFromJson = mapper.readValue(entityJson, this.entityClass);
        ResponseEntity<T> response = addNewEntity(entityFromJson);

        log.debug("Entity(" + this.entityClass.getName() + ") from response:" + response.getBody());
        assertEquals(httpStatus, response.getStatusCode().value());
    }

    protected void testUpdateEntityBadId(String entityJson) throws IOException {
        T entityFromJson = mapper.readValue(entityJson, entityClass);
        T entityExpected = addNewEntity(entityFromJson).getBody();
        entityExpected.setId(0);

        ResponseEntity<T> response = update(entityExpected);

        log.debug("testUpdateEntityBadId - http code: :" + response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    protected void testGetEntityByIdBad() {
        int badId = Integer.MIN_VALUE;
        ResponseEntity<T> response = getById(badId);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    protected void testDeleteEntityByIdBad() {
        int badId = Integer.MIN_VALUE;
        ResponseEntity<CommonMessage> response = deleteById(badId);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    protected ResponseEntity<List<T>> getAll() {
        ResponseEntity<List<T>> response = restTemplate.exchange(
                entityUrl,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                new ParameterizedTypeReference<List<T>>() {
                });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());

        return response;
    }

    protected ResponseEntity<T> update(BaseEntity entity) {

        ResponseEntity<T> response = restTemplate.exchange(
                entityUrl,
                HttpMethod.PUT,
                new HttpEntity(entity, this.httpHeaders),
                this.entityClass);
        return response;
    }

    protected ResponseEntity<T> addNewEntity(BaseEntity entity) {

        ResponseEntity<T> response = restTemplate.postForEntity(
                entityUrl,
                new HttpEntity(entity, this.httpHeaders),
                this.entityClass);

        log.debug("addNewDocRepoEntity - http code:" + response.getStatusCode() +
                ", entity id: " + response.getBody().getId());
        if (response.getStatusCode() == HttpStatus.OK) {
            int id = response.getBody().getId();
            idEntitySet.add(id);
        }
        return response;
    }

    protected ResponseEntity<T> getById(int id) {
        ResponseEntity<T> response = restTemplate.exchange(
                entityUrl + "/{id}",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                this.entityClass,
                id);
        return response;
    }

    protected ResponseEntity<CommonMessage> deleteById(int id) {
        ResponseEntity<CommonMessage> response = restTemplate.exchange(
                entityUrl + "/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(httpHeaders),
                CommonMessage.class,
                id);
        if (response.getStatusCode() == HttpStatus.OK) {
            log.debug("Удален entity id: " + id);
        }
        return response;
    }
}
