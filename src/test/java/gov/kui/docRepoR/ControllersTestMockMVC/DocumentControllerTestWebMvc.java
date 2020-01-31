package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.config.security.JwtAuthenticationEntryPoint;
import gov.kui.docRepoR.config.security.JwtTokenUtil;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.IT.JsonDocuments;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.io.IOException;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

//@WebMvcTest(DocumentController.class)
//@Import(SecurityConfig.class)
public class DocumentControllerTestWebMvc {

    @MockBean
    FileEntityService fileEntityService;

    @MockBean
    JwtAuthenticationEntryPoint unauthorizedHandler;

    @MockBean
    DataSource securityDataSource;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    @Qualifier("userServiceImpl")
    UserDetailsService userDetailsService;

    @MockBean
    private DocumentService documentService;

    @Autowired
    private MockMvc mockMvc;

    private Document validDocument;

    @BeforeEach
    void setUp() throws IOException {
        validDocument = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDocuments.JSON_GOOD_2_SENDERS.toString(), Document.class);
    }

    @AfterEach
    void tearDown() {
        reset(documentService);
    }

    @Test
    void testGetDocumentById() throws Exception {
    //    given(jwtTokenUtil.validateToken(any(),any())).willReturn(true);
     //   given(documentService.findById(anyInt())).willReturn(validDocument);
        when(documentService.findById(anyInt())).thenReturn(validDocument);

//        Document actualDoc = documentService.findById(1);
//        System.out.println("-----actualDoc:"+actualDoc);
//        assertNotNull(actualDoc);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                DocRepoURL.DOCUMENTS_LOCALHOST.toString() + "/" + validDocument.getId());
        requestBuilder.contentType(MediaType.APPLICATION_JSON_UTF8);

        MockHttpServletRequestBuilder spyRequest = MockMvcRequestBuilders.get(
                "http://localhost:8080/api/spy");

        MvcResult result = mockMvc.perform(spyRequest)
                .andExpect(status().isOk())
                .andDo(print())
 //               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
 //           .andExpect(jsonPath("$.id", is(validDocument.getId())))
                .andExpect(content().string(containsString("I'm a spy of the Red Army!")))
                .andReturn();
        System.err.println("result: " + result.getResponse().getContentAsString());
    }
}
