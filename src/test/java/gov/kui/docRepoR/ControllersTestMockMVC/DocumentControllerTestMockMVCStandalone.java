package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.JsonDocument;
import gov.kui.docRepoR.controller.DocumentController;
import gov.kui.docRepoR.domain.DocumentRandomFactory;
import gov.kui.docRepoR.dto.DocumentDto;
import gov.kui.docRepoR.facade.DocumentServiceFacade;
import gov.kui.docRepoR.service.DocumentService;
import gov.kui.docRepoR.service.FileEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Mock
    private DocumentServiceFacade documentServiceFacade;

    @InjectMocks
    private DocumentController documentController;

    @Captor
    ArgumentCaptor<DocumentDto> captorDocument = ArgumentCaptor.forClass(DocumentDto.class);

    private MockMvc mockMvc;
    private Document validDocument;
    private DocumentDto validDocumentDto;

    @BeforeEach
    void setUp() throws IOException {

        validDocument = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDocument.JSON_GOOD.toString(), Document.class);

        validDocumentDto = DocumentRandomFactory.getDtoFromDocument(validDocument);

        mockMvc = MockMvcBuilders.standaloneSetup(documentController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter()).build();
    }

    @Test
    void testGetAllDocuments() throws Exception {

        List<DocumentDto> documentDtos = new ArrayList<>();
        documentDtos.add(validDocumentDto);

        given(documentServiceFacade.findAll()).willReturn(documentDtos);

        mockMvc.perform(get(DocRepoURL.DOCUMENTS_LOCALHOST.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()", is(documentDtos.size())))
                .andExpect(jsonPath("$[0].id", is(validDocument.getId())))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetDocumentById() throws Exception {

        given(documentServiceFacade.findById(anyInt())).willReturn(validDocumentDto);

        mockMvc.perform(get(DocRepoURL.DOCUMENTS_LOCALHOST.toString() + "/" + validDocument.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validDocument.getId())));
    }

    @Test
    void testAddDocumentOk() throws Exception {

        given(documentServiceFacade.save(any())).willReturn(validDocumentDto);

        mockMvc.perform(post(DocRepoURL.DOCUMENTS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDocument.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        then(documentServiceFacade).should().save(captorDocument.capture());
        assertEquals(0,captorDocument.getValue().getId());
    }

    @Test
    void testAddDocumentBadNull() throws Exception {

        mockMvc.perform(post(DocRepoURL.DOCUMENTS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDocument.JSON_NULL.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testUpdateDocumentZeroIdBad() throws Exception {

        mockMvc.perform(put(DocRepoURL.DOCUMENTS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDocument.JSON_ZERO_ID.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testUpdateDocumentOK() throws Exception {
        given(documentService.save(any())).willReturn(validDocument);

        mockMvc.perform(put(DocRepoURL.DOCUMENTS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDocument.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
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

//        FileEntity fileEntity = new FileEntity();
//        fileEntity.setFilename("file.pdf");
//        fileEntity.setContentType("application/pdf");
//        fileEntity.setFileSize(12345);
//        fileEntity.setDocumentId(validDocument.getId());
//
//        Set<FileEntity> fileEntities = new HashSet<>();
//        fileEntities.add(fileEntity);
//
//        given(fileEntityService.findByDocId(anyInt())).willReturn(fileEntities);
//
//        mockMvc.perform(get(DocRepoURL.DOCUMENTS_LOCALHOST.toString() + "/files/" + validDocument.getId()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}
