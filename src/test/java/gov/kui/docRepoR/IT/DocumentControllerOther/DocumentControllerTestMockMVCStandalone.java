package gov.kui.docRepoR.IT.DocumentControllerOther;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.model.Document;
import gov.kui.docRepoR.IT.JsonDocuments;
import gov.kui.docRepoR.controller.DocumentController;
import gov.kui.docRepoR.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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
    DocumentController documentController;
    MockMvc mockMvc;
    Document validDocument;

    @BeforeEach
    void setUp() throws IOException {
        validDocument = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);
        mockMvc = MockMvcBuilders.standaloneSetup(documentController)
                .setMessageConverters(jackson2HttpMessageConverter()).build();
    }

    @Test
    void testGetAllDocuments() throws Exception {
        List<Document> documentList = new ArrayList<>();
        documentList.add(validDocument);

        given(documentService.findAll()).willReturn(documentList);

        mockMvc.perform(get(DocRepoURL.DOCUMENTS_LOCALHOST.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()", is(documentList.size())))
                .andExpect(jsonPath("$[0].id", is(validDocument.getId())))
                .andExpect(jsonPath("$", hasSize(1)));
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

    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.registerModule(new JavaTimeModule());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
