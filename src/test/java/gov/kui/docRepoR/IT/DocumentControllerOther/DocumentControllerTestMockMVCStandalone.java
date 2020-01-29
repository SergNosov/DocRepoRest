package gov.kui.docRepoR.IT.DocumentControllerOther;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.IT.JsonDocuments;
import gov.kui.docRepoR.controller.DocumentController;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class DocumentControllerTestMockMVCStandalone {

    @Mock
    private FileEntityService fileEntityService;

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    private MockMvc mockMvc;
    private Document validDocument;

    @BeforeEach
    void setUp() throws IOException {

        validDocument = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDocuments.JSON_GOOD.toString(), Document.class);

        mockMvc = MockMvcBuilders.standaloneSetup(documentController)
                .setControllerAdvice(new RestExceptionHandler())
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

        mockMvc.perform(get(DocRepoURL.DOCUMENTS_LOCALHOST.toString() + "/" + validDocument.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validDocument.getId())));
    }

    @Test
    void testAddDocumentOk() throws Exception {

        given(documentService.save(any())).willReturn(validDocument);

        mockMvc.perform(post(DocRepoURL.DOCUMENTS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDocuments.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testAddDocumentBadNull() throws Exception {

        mockMvc.perform(post(DocRepoURL.DOCUMENTS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDocuments.JSON_NULL.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testUpdateDocumentZeroIdBad() throws Exception {

        mockMvc.perform(put(DocRepoURL.DOCUMENTS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDocuments.JSON_ZERO_ID.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testDeleteDocument() throws Exception {
        given(documentService.deleteById(anyInt())).willReturn(validDocument.getId());

        mockMvc.perform(delete(DocRepoURL.DOCUMENTS_LOCALHOST.toString() + "/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Удален документ id - " + validDocument.getId())));
    }

    @Test
    void testGetDocFiles() throws Exception {

        FileEntity fileEntity = new FileEntity("test.jpg", "jpg", 12345, validDocument.getId());
        Set<FileEntity> fileEntities = new HashSet<FileEntity>();
        fileEntities.add(fileEntity);

        given(fileEntityService.findByDocId(anyInt())).willReturn(fileEntities);

        mockMvc.perform(get(DocRepoURL.DOCUMENTS_LOCALHOST.toString() + "/files/" + validDocument.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.registerModule(new JavaTimeModule());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
