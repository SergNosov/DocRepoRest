package gov.kui.docRepoR.DocumentControllerTests;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.Entity.Document;
import gov.kui.docRepoR.controller.DocumentRestController;
import gov.kui.docRepoR.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class DocumentControllerTestMockMVCStandalone {

    @Mock
    DocumentService documentService;

    @InjectMocks
    DocumentRestController documentRestController;
    MockMvc mockMvc;
    Document validDocument;

    @BeforeEach
    void setUp() throws IOException {
        validDocument = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        mockMvc = MockMvcBuilders.standaloneSetup(documentRestController).build();
    }

    @Test
    void tesAllDocuments() throws Exception {
        mockMvc.perform(get(DocRepoURL.DOCUMENTS.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testGetDocumentById() throws Exception {
        given(documentService.findById(anyInt())).willReturn(validDocument);

        mockMvc.perform(get(DocRepoURL.DOCUMENTS.toString() + "/" + validDocument.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validDocument.getId())));
    }
}
