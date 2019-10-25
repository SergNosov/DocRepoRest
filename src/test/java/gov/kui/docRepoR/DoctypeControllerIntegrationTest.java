package gov.kui.docRepoR;

import gov.kui.docRepoR.Entity.Doctype;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.List;

public class DoctypeControllerIntegrationTest {
    /*
    private static final String ROOT = "http://localhost:8080/api/doctypes";
    private RestTemplate restTemplate;
    private HttpHeaders headers;

    public DoctypeControllerIntegrationTest(){
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    public void checkIsJSON() throws IOException {
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet(ROOT);
        HttpResponse response = HttpClientBuilder.create().build().execute( request );
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals( jsonMimeType, mimeType );
    }

    @Test
    public void checkGetAllDoctypes(){
        ResponseEntity<List<Doctype>> responseEntity = getResponseAllDoctypes();
        List<Doctype> receivedDoctypes = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(receivedDoctypes);
        assertFalse(receivedDoctypes.isEmpty());
    }

    @Test
    public void checkGetDoctypeById_OK() {
        Doctype tempDoctype = getResponseAllDoctypes().getBody().get(0);
        assertNotNull(tempDoctype);

        final int id = tempDoctype.getId();

        Doctype responseDoctype = restTemplate.getForObject(ROOT + "/" + id, Doctype.class);
        assertNotNull(responseDoctype);
        assertEquals(responseDoctype.getId(), tempDoctype.getId());
        assertEquals(responseDoctype.getTitle(), tempDoctype.getTitle());
    }

    private ResponseEntity<List<Doctype>> getResponseAllDoctypes(){
        return restTemplate.exchange(
                ROOT,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Doctype>>() {
                }
        );
    }

     */
}
