package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.JsonDocument;
import gov.kui.docRepoR.service.DocumentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class DocumentControllerTestWebMvc {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private Document validDocument;

    @MockBean
    private DocumentService documentService;

    @BeforeEach
    void setUp() throws IOException {
        validDocument = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDocument.JSON_GOOD.toString(), Document.class);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).dispatchOptions(true).build();
    }

    @AfterEach
    void tearDown() {
        reset(documentService);
    }

    @Test
    void testGetDocumentById() throws Exception {
        when(documentService.findById(anyInt())).thenReturn(validDocument);

        mockMvc.perform(get(DocRepoURL.DOCUMENTS_LOCALHOST.toString() + "/1000")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
              //  .with(SecurityRequestPostProcessors.userDetailsService("john"))
        )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
