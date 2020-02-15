package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.config.security.JwtAuthenticationEntryPoint;
import gov.kui.docRepoR.config.security.JwtTokenUtil;
import gov.kui.docRepoR.controller.DocumentController;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.JsonDocument;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import gov.kui.docRepoR.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
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
    UserService userService;

    @MockBean
    private DocumentService documentService;

    @Autowired
    private MockMvc mockMvc;

    private Document validDocument;

    @BeforeEach
    void setUp() throws IOException {
        validDocument = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDocument.JSON_GOOD_2_SENDERS.toString(), Document.class);
    }

    @AfterEach
    void tearDown() {
        reset(documentService);
    }

    @Test
    void testGetDocumentById() throws Exception {
        when(documentService.findById(anyInt())).thenReturn(validDocument);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                DocRepoURL.DOCUMENTS_LOCALHOST.toString() + "/" + validDocument.getId());
        requestBuilder.contentType(MediaType.APPLICATION_JSON_UTF8);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }
}
