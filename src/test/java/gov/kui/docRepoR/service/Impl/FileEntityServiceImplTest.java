package gov.kui.docRepoR.service.Impl;

import com.google.common.collect.Lists;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.dao.FileEntityRepository;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.FileByte;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class FileEntityServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private FileEntityRepository fileEntityRepository;

    @InjectMocks
    private FileEntityServiceImpl fileEntityService;

    private FileEntity fileEntity;
    private FileEntityDto fileEntityDto;

    @BeforeEach
    void setUp() {
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
    @DisplayName("1. Testing the receipt of all fileEntity.")
    @Order(1)
    void findAll() {

        List<FileEntity> filesData = new ArrayList<>();
        filesData.add(fileEntity);

        when(fileEntityRepository.findAll()).thenReturn(filesData);

        List<FileEntity> fileEntities = fileEntityService.findAll();
        assertEquals(fileEntities.size(), 1);
        verify(fileEntityRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("2. Testing the receipt of fileEntity by id. OK.")
    @Order(2)
    void findById() {

        this.fileEntity.setFileByte(new FileByte(new byte[]{1, 2, 3}));

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.of(this.fileEntity));
        FileEntity actualFileEntity = fileEntityService.findById(Integer.MIN_VALUE);

        System.out.println("expected.id: " + fileEntity.getId() + " (identityHashCode: " +
                System.identityHashCode(fileEntity) + "); " +
                "actual.id: " + actualFileEntity.getId() + " (identityHashCode: " +
                System.identityHashCode(actualFileEntity) + ");");

        verify(fileEntityRepository, times(1)).findById(anyInt());
        assertAll(
                () -> assertEquals(fileEntity, actualFileEntity),
                () -> assertNotNull(actualFileEntity),
                () -> assertEquals(fileEntity.getFilename(), actualFileEntity.getFilename()),
                () -> assertEquals(fileEntity.getContentType(), actualFileEntity.getContentType()),
                () -> assertEquals(fileEntity.getFileSize(), actualFileEntity.getFileSize()),
                () -> assertEquals(fileEntity.getDocumentId(), actualFileEntity.getDocumentId()),
                () -> assertEquals(fileEntity.getFileByte().getBytes().length,
                        actualFileEntity.getFileByte().getBytes().length)
        );
    }

    @Test
    @DisplayName("3. Testing the receipt of fileEntityDto by id. OK.")
    @Order(3)
    void findDtoByIdOk() {
        when(fileEntityRepository.findDtoById(anyInt())).thenReturn(Optional.of(fileEntityDto));
        FileEntityDto fileEntityDtoActual = fileEntityService.findDtoById(Integer.MIN_VALUE);

        verify(fileEntityRepository, times(1)).findDtoById(anyInt());
        assertNotNull(fileEntityDtoActual);
        assertEquals(fileEntityDto.getId(), fileEntityDtoActual.getId());
        assertEquals(fileEntityDto.getFilename(), fileEntityDtoActual.getFilename());
        assertEquals(fileEntityDto.getFileSize(), fileEntityDtoActual.getFileSize());
    }

    @Test
    @DisplayName("4. Testing the receipt of fileEntity by id. BAD.")
    @Order(4)
    void findByIdNotFound() {

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.empty());

        IllegalArgumentException rte = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.findById(Integer.MIN_VALUE));
        assertEquals("Не найден файл (fileEntity) с id - " + Integer.MIN_VALUE, rte.getMessage());
    }

    @Test
    @DisplayName("5. Testing the receipt of fileEntityDto by id. BAD.")
    @Order(5)
    void findDtoByIdNotFound() {
        when(fileEntityRepository.findDtoById(anyInt())).thenReturn(Optional.empty());

        IllegalArgumentException rte = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.findDtoById(Integer.MIN_VALUE));

        assertEquals("Не найден файл (fileEntityDto) с id - " + Integer.MIN_VALUE, rte.getMessage());
    }

    @Test
    @DisplayName("6. Testing the receipt of fileEntityDtos by documentId. OK.")
    @Order(6)
    void findByDocId() {
        List<FileEntityDto> fileEntityDtos = Lists.newArrayList(fileEntityDto);

        when(fileEntityRepository.findFileEntityDtosByDocId((anyInt()))).thenReturn(fileEntityDtos);

        List<FileEntityDto> actualFileEntitieDtos = fileEntityService.findDtosByDocId(Integer.MIN_VALUE);

        verify(fileEntityRepository, times(1)).findFileEntityDtosByDocId(anyInt());
        assertFalse(actualFileEntitieDtos.isEmpty());
    }

    @Test
    @DisplayName("7. Testing the save of fileEntity. OK.")
    @Order(7)
    void saveOk() {
        this.fileEntity.setId(0);
        this.fileEntity.setFileByte(new FileByte(new byte[]{1, 2, 3}));
        this.fileEntity.setFileSize(this.fileEntity.getFileByte().getBytes().length);

        FileEntity fileEntityWithNonZeroId = new FileEntity();
        fileEntityWithNonZeroId.setId(1);
        fileEntityWithNonZeroId.setFilename("file.pdf");
        fileEntityWithNonZeroId.setContentType("application/pdf");
        fileEntityWithNonZeroId.setDocumentId(1);
        fileEntityWithNonZeroId.setFileByte(new FileByte(new byte[]{1, 2, 3}));
        fileEntityWithNonZeroId.setFileSize(fileEntityWithNonZeroId.getFileByte().getBytes().length);

        Document mockDoc = new Document();
        mockDoc.setId(1);

        when(documentRepository.findById(anyInt())).thenReturn(Optional.of(mockDoc));
        when(fileEntityRepository.save(any(FileEntity.class))).thenReturn(fileEntityWithNonZeroId);

        FileEntity actualFileEntity = fileEntityService.save(this.fileEntity);

        assertAll(
                () -> assertNotNull(actualFileEntity),
                () -> assertNotEquals(fileEntity.getId(), actualFileEntity.getId()),
                () -> assertEquals(fileEntity.getFilename(), actualFileEntity.getFilename()),
                () -> assertEquals(fileEntity.getContentType(), actualFileEntity.getContentType()),
                () -> assertEquals(fileEntity.getFileSize(), actualFileEntity.getFileSize()),
                () -> assertEquals(fileEntity.getDocumentId(), actualFileEntity.getDocumentId()),
                () -> assertEquals(fileEntity.getFileByte().getBytes().length,
                        actualFileEntity.getFileByte().getBytes().length)
        );

        verify(fileEntityRepository, times(1)).save(any(FileEntity.class));
        verify(documentRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("8. Testing the save of fileEntity=null. BAD.")
    @Order(8)
    void saveNullBad() {

        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.save(null)
        );
        assertEquals("fileEntity is null", iaeNull.getMessage());
        verify(fileEntityRepository, times(0)).save(any(FileEntity.class));
        verify(documentRepository, times(0)).findById(anyInt());
    }

    @Test
    @DisplayName("9. Testing the save of fileEntity.fileNameIsEmpty. BAD.")
    @Order(9)
    void saveFileNameEmptyBad() {

        this.fileEntity.setFilename(" ");
        this.fileEntity.setFileByte(new FileByte(new byte[]{1, 2, 3}));

        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.save(this.fileEntity)
        );

        assertEquals("Не верно указаны реквизиты файла filename: " +
                this.fileEntity.getFilename(), iaeNull.getMessage());
        verify(fileEntityRepository, times(0)).save(any(FileEntity.class));
    }

    @Test
    @DisplayName("10. Testing the save of fileEntity. Attached File is Empty. BAD.")
    @Order(10)
    void saveEmptyFileBad() {

        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.save(this.fileEntity)
        );

        assertEquals("Не добавлен файл:" + fileEntity.getFilename(), iaeNull.getMessage());
        verify(fileEntityRepository, times(0)).save(any(FileEntity.class));
    }

    @Test
    @DisplayName("11. Testing the save of fileEntity. The fileSize is incorrect value.")
    @Order(11)
    void saveFileEntityWithOtherFileLength() {

        this.fileEntity.setFileByte(new FileByte(new byte[]{1, 2, 3, 4}));
        System.out.println("---- До сохранения (this.fileEntity.getFileSize()): " + this.fileEntity.getFileSize());

        when(documentRepository.findById(anyInt())).thenReturn(Optional.of(new Document()));

        fileEntityService.save(this.fileEntity);
        System.out.println("---- После сохранения (this.fileEntity.getFileSize()): " + this.fileEntity.getFileSize());
        assertEquals(4, this.fileEntity.getFileSize());
        verify(fileEntityRepository, times(1)).save(any(FileEntity.class));
        verify(documentRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("12. Testing saving a fileEntity with an invalid document id.")
    @Order(12)
    void saveFileEntityBadDocId() {
        this.fileEntity.setFileByte(new FileByte(new byte[]{1, 2, 3, 4}));

        when(documentRepository.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException re = assertThrows(RuntimeException.class,
                () -> fileEntityService.save(this.fileEntity)
        );
        assertEquals("Не найден документ с id - " + fileEntity.getDocumentId(), re.getMessage());
        verify(fileEntityRepository, times(0)).save(any(FileEntity.class));
    }

    @Test
    @DisplayName("13. Testing deleting of fileEntity by id. OK.")
    @Order(13)
    void deleteByIdOk() {

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.of(this.fileEntity));

        int deletedId = fileEntityService.deleteById(this.fileEntity.getId());

        assertEquals(this.fileEntity.getId(), deletedId);
        verify(fileEntityRepository, times(1)).findById(anyInt());
        verify(fileEntityRepository, times(1)).deleteById(anyInt());
    }

    @Test
    @DisplayName("14. Testing deleting of fileEntity by bad id. BAD.")
    @Order(14)
    void deleteByIdBAD() {

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException re = assertThrows(RuntimeException.class,
                () -> fileEntityService.deleteById(this.fileEntity.getId())
        );

        assertEquals("Не найден файл (fileEntity) с id - " + fileEntity.getId(), re.getMessage());

        verify(fileEntityRepository, times(1)).findById(anyInt());
        verify(fileEntityRepository, times(0)).deleteById(anyInt());
    }
}
