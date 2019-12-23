package gov.kui.docRepoR.IT.DocumentControllerOther;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.model.Document;
import gov.kui.docRepoR.IT.JsonDocuments;
import gov.kui.docRepoR.controller.DocumentController;
import gov.kui.docRepoR.service.DocumentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import java.io.IOException;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
public class DocumentControllerTestWebMvc {

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
        given(documentService.findById(anyInt())).willReturn(validDocument);

        MvcResult result = mockMvc.perform(get(DocRepoURL.DOCUMENTS_LOCALHOST.toString() + "/" + validDocument.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validDocument.getId())))
                .andReturn();
        System.err.println("result: " + result.getResponse().getContentAsString());
    }
}
