package gov.kui.docRepoR.facade.Impl;

import com.google.common.collect.Lists;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.FileEntityBlob;
import gov.kui.docRepoR.domain.FileEntityRandomFactory;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.mappers.FileEntityMapper;
import gov.kui.docRepoR.service.FileEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class FileEntityServiceFacadeTests {

    @Mock
    private FileEntityMapper fileEntityMapper;

    @Mock
    private FileEntityService fileEntityService;

    @InjectMocks
    private FileEntityServiceFacadeImpl fileEntityServiceFacade;

    private FileEntityDto fileEntityDto;
    private FileEntityBlob fileEntityBlob;

    @BeforeEach
    void setUp() {
        fileEntityBlob = FileEntityRandomFactory.getRandomFileEntityBlob(new Random().nextInt(100));

        fileEntityDto = FileEntityDto.builder()
                .id(fileEntityBlob.getFileEntity().getId())
                .filename(fileEntityBlob.getFileEntity().getFilename())
                .fileSize(fileEntityBlob.getFileEntity().getFileSize())
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("1. Testing the receipt of fileEntityDto by id. OK.")
    void findByIdTestOk() {
        given(fileEntityService.findDtoById(anyInt())).willReturn(fileEntityDto);
        FileEntityDto fileEntityDtoActual = fileEntityServiceFacade.findDtoById(fileEntityDto.getId());

        then(fileEntityService).should(times(1)).findDtoById(anyInt());
        assertNotNull(fileEntityDtoActual);
        assertEquals(fileEntityDto.getId(), fileEntityDtoActual.getId());
        assertEquals(fileEntityDto.getFilename(), fileEntityDtoActual.getFilename());
        assertEquals(fileEntityDto.getFileSize(), fileEntityDtoActual.getFileSize());
    }

    @Test
    @Order(2)
    @DisplayName("2. Testing the receipt of fileEntityDto by id. Not found.")
    void findByIdNotFoundTest() {
        given(fileEntityService.findDtoById(anyInt()))
                .willThrow(new IllegalArgumentException("Не найден файл (fileEntityDto) с id - " + fileEntityDto.getId()));

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> fileEntityServiceFacade.findDtoById(fileEntityDto.getId())
        );

        then(fileEntityService).should(times(1)).findDtoById(anyInt());
        assertEquals("Не найден файл (fileEntityDto) с id - " + fileEntityDto.getId(), iae.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("3. Testing the receipt of all fileEntityDtos. Ok")
    void findDtosByDocId() {

        List<FileEntityDto> fileEntityDtos = Lists.newArrayList(fileEntityDto);
        given(fileEntityService.findDtosByDocId(anyInt())).willReturn(fileEntityDtos);

        List<FileEntityDto> fileEntityDtosActual = fileEntityServiceFacade.findDtosByDocId(
                fileEntityBlob.getFileEntity().getDocumentId()
        );

        then(fileEntityService).should(times(1)).findDtosByDocId(anyInt());
        assertNotNull(fileEntityDtosActual);
        assertFalse(fileEntityDtosActual.isEmpty());
        assertEquals(fileEntityDtos.size(), fileEntityDtosActual.size());
    }

    @Test
    @Order(4)
    @DisplayName("4. Test delete of fileEntity by id. Ok.")
    void deleteByIdTest() {
        given(fileEntityService.deleteById(anyInt())).willReturn(fileEntityDto.getId());

        int deletedId = fileEntityServiceFacade.deleteById(fileEntityDto.getId());

        then(fileEntityService).should(times(1)).deleteById(fileEntityDto.getId());
        assertEquals(fileEntityDto.getId(), deletedId);
    }

    @Test
    @Order(5)
    @DisplayName("5. Test save of fileEntity. Ok.")
    void saveFileEntityDtoTest(){
        given(fileEntityService.save(any())).willReturn(fileEntityBlob.getFileEntity());
        given(fileEntityMapper.fileEntityToFileEntityDto(any(FileEntity.class))).willReturn(fileEntityDto);

        FileEntityDto fileEntityDtoActual = fileEntityServiceFacade.save(fileEntityBlob);

        then(fileEntityService).should(times(1)).save(any(FileEntityBlob.class));
        then(fileEntityMapper).should(times(1)).fileEntityToFileEntityDto(any(FileEntity.class));
        assertNotNull(fileEntityDtoActual);
        assertEquals(fileEntityDto.getId(),fileEntityDtoActual.getId());
        assertEquals(fileEntityDto.getFilename(),fileEntityDtoActual.getFilename());
        assertEquals(fileEntityDto.getFileSize(),fileEntityDtoActual.getFileSize());
    }

    @Test
    @Order(6)
    @DisplayName("6. Test save of fileEntity null. Bad.")
    void saveFileEntityTestNull(){
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                ()->fileEntityServiceFacade.save(null));

        then(fileEntityService).should(times(0)).save(any(FileEntityBlob.class));
        then(fileEntityMapper).should(times(0)).fileEntityToFileEntityDto(any(FileEntity.class));
        assertEquals("Не указан fileEntity (null)", iae.getMessage());
    }
}
