package gov.kui.docRepoR;

import gov.kui.docRepoR.Entity.Doctype;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DoctypeControllerSpringIntegrationTest {

    private static final String ROOT = "http://localhost:8080/api/doctypes";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void checkGetAllDoctypes(){
        ResponseEntity<List<Doctype>> response = restTemplate.exchange(ROOT,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Doctype>>() {});
        List<Doctype> doctypes = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(doctypes);
        assertFalse(doctypes.isEmpty());

      //  assertThat(persons, hasSize(2));
    }
}
