package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.JsonFileEntity;
import gov.kui.docRepoR.controller.FileController;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.service.FileEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FileControllerTestMockMVCStandalone {

    @Mock
    private FileEntityService fileEntityService;

    @InjectMocks
    private FileController fileController;

    private MockMvc mockMvc;
    private FileEntity validFileEntity;
    private MockMultipartFile multipartFile;

    @BeforeEach
    void setUp() throws IOException {

        multipartFile = new MockMultipartFile(
                "file",
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );

        validFileEntity = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonFileEntity.JSON_GOOD.toString(), FileEntity.class);

        mockMvc = MockMvcBuilders.standaloneSetup(fileController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter(),
                        new ResourceHttpMessageConverter())
                .build();
    }

    @Test
    public void testGetFileEntityByIdOk() throws Exception {

        given(fileEntityService.findById(anyInt())).willReturn(validFileEntity);

        mockMvc.perform(get(DocRepoURL.FILE_LOCALHOST + "/" + validFileEntity.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validFileEntity.getId())));
    }

    @Test
    public void testGetFileEntityByIdBad() throws Exception {

        given(fileEntityService.findById(anyInt())).willThrow(
                new IllegalArgumentException("Не найден файл (fileEntity) с id - " + validFileEntity.getId())
        );

        mockMvc.perform(get(DocRepoURL.FILE_LOCALHOST + "/" + validFileEntity.getId()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message",
                        containsString("Не найден файл (fileEntity) с id - " + validFileEntity.getId())
                ));
    }

    @Test
    public void testDeleteFileEntity() throws Exception {
        given(fileEntityService.deleteById(anyInt())).willReturn(validFileEntity.getId());

        mockMvc.perform(delete(DocRepoURL.FILE_LOCALHOST + "/" + validFileEntity.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message",
                        containsString("Удален файл id - " + validFileEntity.getId())
                ));
    }

    @Test
    public void testUploadFileOk() throws Exception {

        Document mockDoc = new Document();
        mockDoc.setId(21);

        FileEntity fileEntityExpected = FileEntity.getInstance(multipartFile, mockDoc.getId());

        given(fileEntityService.save(any(FileEntity.class))).willReturn(fileEntityExpected);

        mockMvc.perform(multipart(DocRepoURL.FILE_LOCALHOST + "/" + mockDoc.getId())
                .file(multipartFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filename", is(fileEntityExpected.getFilename())))
                .andExpect(jsonPath("$.contentType", is(fileEntityExpected.getContentType())))
                .andExpect(jsonPath("$.fileSize", is((int) fileEntityExpected.getFileSize())))
                .andExpect(jsonPath("$.documentId", is(fileEntityExpected.getDocumentId())));
    }

    @Test
    public void testGetFileOk() throws Exception {

        FileEntity fileEntity = FileEntity.getInstance(multipartFile, 21);

        given(fileEntityService.findById(anyInt())).willReturn(fileEntity);

        mockMvc.perform(get(DocRepoURL.FILE_LOCALHOST + "/load/21"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFileBad() throws Exception {

        FileEntity fileEntity = FileEntity.getInstance(multipartFile, 21);
        fileEntity.setFileByte(null);

        given(fileEntityService.findById(anyInt())).willReturn(fileEntity);

        mockMvc.perform(get(DocRepoURL.FILE_LOCALHOST + "/load/21"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
