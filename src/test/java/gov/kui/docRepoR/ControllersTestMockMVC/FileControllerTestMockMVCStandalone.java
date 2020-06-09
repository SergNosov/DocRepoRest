package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.JsonFileEntity;
import gov.kui.docRepoR.controller.FileController;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.FileEntityBlob;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.facade.FileEntityServiceFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FileControllerTestMockMVCStandalone {

    @Mock
    private FileEntityServiceFacade fileEntityServiceFacade;

    @InjectMocks
    private FileController fileController;

    private MockMvc mockMvc;
    private FileEntity validFileEntity;
    private FileEntityDto fileEntityDto;
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

        fileEntityDto = FileEntityDto.builder()
                .id(validFileEntity.getId())
                .filename(validFileEntity.getFilename())
                .contentType(validFileEntity.getContentType())
                .fileSize(validFileEntity.getFileSize())
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(fileController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter(),
                        new ResourceHttpMessageConverter())
                .build();
    }

    @Test
    public void testGetFileEntityByIdOk() throws Exception {

        given(fileEntityServiceFacade.findDtoById(anyInt())).willReturn(fileEntityDto);

        mockMvc.perform(get(DocRepoURL.FILE_LOCALHOST + "/" + validFileEntity.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(fileEntityDto.getId())));
    }

    @Test
    public void testGetFileEntityByIdBad() throws Exception {

        given(fileEntityServiceFacade.findDtoById(anyInt())).willThrow(
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
        given(fileEntityServiceFacade.deleteById(anyInt())).willReturn(validFileEntity.getId());

        mockMvc.perform(delete(DocRepoURL.FILE_LOCALHOST + "/" + validFileEntity.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message",
                        containsString("Удален файл id - " + validFileEntity.getId())
                ));
    }

    @Test
    public void testUploadFileOk() throws Exception {
        final int docId = 21;
        FileEntityBlob fileEntityBlobExpected = FileEntityBlob.getInstance(multipartFile, docId);

        FileEntityDto fileEntityDtoExpected = FileEntityDto.builder()
                .id(fileEntityBlobExpected.getId())
                .filename(fileEntityBlobExpected.getFileEntity().getFilename())
                .contentType(fileEntityBlobExpected.getFileEntity().getContentType())
                .fileSize(fileEntityBlobExpected.getFileEntity().getFileSize())
                .build();

        given(fileEntityServiceFacade.save(any())).willReturn(fileEntityDtoExpected);

        mockMvc.perform(multipart(DocRepoURL.FILE_LOCALHOST + "/" + docId)
                .file(multipartFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filename", is(fileEntityDtoExpected.getFilename())))
                .andExpect(jsonPath("$.contentType", is(fileEntityDtoExpected.getContentType())))
                .andExpect(jsonPath("$.fileSize", is((int) fileEntityDtoExpected.getFileSize())));
    }

    @Test
    @Disabled
    public void testGetFileOk() throws Exception {

        FileEntity fileEntity = FileEntity.getInstance(multipartFile, 21);

        given(fileEntityServiceFacade.findDtoById(anyInt())).willReturn(fileEntityDto);

        mockMvc.perform(get(DocRepoURL.FILE_LOCALHOST + "/load/21"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    public void testGetFileBad() throws Exception {
        given(fileEntityServiceFacade.getResourseById(anyInt())).willReturn(ResponseEntity.noContent().build());

        mockMvc.perform(get(DocRepoURL.FILE_LOCALHOST + "/load/21"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
