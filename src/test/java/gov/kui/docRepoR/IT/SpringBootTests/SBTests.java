package gov.kui.docRepoR.IT.SpringBootTests;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.service.DoctypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SBTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private DoctypeService doctypeService;

    @Test
    void testGetDoctypeById() {
        Doctype doctype = new Doctype("qqq");

        given(doctypeService.findById(anyInt())).willReturn(doctype);
        System.out.println("---doctype: " + doctypeService.findById(2));

        Doctype doctypeActual = restTemplate.getForObject(
                "http://localhost:" + port + "//api//doctypes//2",
                Doctype.class
        );

        System.out.println("---doctypeActual: " + doctypeActual);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ResponseEntity<Doctype> response = restTemplate.exchange(
                "http://localhost:" + port + "//api//doctypes//2",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                Doctype.class
                );
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }

}
