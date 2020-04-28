package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.service.FileEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class FileEntityServiceFacadeTests {

    @Mock
    private FileEntityService fileEntityService;

    @InjectMocks
    private FileEntityServiceFacadeImpl fileEntityServiceFacade;

    private FileEntityDto fileEntityDto;
    private FileEntity fileEntity;

    @BeforeEach
    void setUp(){
        this.fileEntity = new FileEntity();
        this.fileEntity.setId(1);
        this.fileEntity.setFilename("file.pdf");
        this.fileEntity.setContentType("application/pdf");
        this.fileEntity.setDocumentId(1);

        fileEntityDto = FileEntityDto.builder()
                .id(fileEntity.getId())
                .filename(fileEntity.getFilename())
                .fileSize(fileEntity.getFileSize())
                .build();
    }

    @Test
    void findByIdTestOk(){
        given(fileEntityService.findDtoById(anyInt())).willReturn(fileEntityDto);
        FileEntityDto fileEntityDtoActual = fileEntityServiceFacade.findById(fileEntityDto.getId());
        assertNotNull(fileEntityDtoActual);
    }
}
