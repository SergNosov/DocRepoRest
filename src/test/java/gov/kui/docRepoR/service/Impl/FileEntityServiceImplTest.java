package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.dao.FileEntityRepository;
import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.domain.FileEntity;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @BeforeEach
    void setUp() {
        this.fileEntity = new FileEntity();
        this.fileEntity.setFilename("file.pdf");
        this.fileEntity.setContentType("application/pdf");
        this.fileEntity.setDocumentId(1);
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

        this.fileEntity.setId(1);
        this.fileEntity.setBytes(new byte[]{1,2,3});

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.of(this.fileEntity));
        FileEntity actualFileEntity = fileEntityService.findById(1);

        System.out.println("expected.id: " + fileEntity.getId() + " (identityHashCode: " +
                System.identityHashCode(fileEntity) + "); " +
                "actual.id: " + actualFileEntity.getId() + " (identityHashCode: " +
                System.identityHashCode(actualFileEntity) + ");");

        assertAll(
                ()-> assertEquals(fileEntity, actualFileEntity),
                () -> assertNotNull(actualFileEntity),
                () -> assertEquals(fileEntity.getFilename(), actualFileEntity.getFilename()),
                () -> assertEquals(fileEntity.getContentType(), actualFileEntity.getContentType()),
                () -> assertEquals(fileEntity.getFileSize(), actualFileEntity.getFileSize()),
                () -> assertEquals(fileEntity.getDocumentId(), actualFileEntity.getDocumentId()),
                () -> assertEquals(fileEntity.getBytes().length, actualFileEntity.getBytes().length)
        );
        verify(fileEntityRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("3. Testing the receipt of fileEntity by id. BAD.")
    @Order(3)
    void findByIdBad() {

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException rte = assertThrows(RuntimeException.class, () -> fileEntityService.findById(1));
        assertEquals("Не найден файл (fileEntity) с id - " + 1, rte.getMessage());
    }

    @Test
    @DisplayName("4. Testing the receipt of fileEntity by documentId. OK.")
    @Order(4)
    void findByDocId() {

        this.fileEntity.setId(1);

        Set<FileEntity> dataFileEntity = new HashSet<>();
        dataFileEntity.add(fileEntity);

        when(fileEntityRepository.findAllByDocumentId(anyInt())).thenReturn(dataFileEntity);

        Set<FileEntity> actualFileEntities = fileEntityService.findByDocId(21);

        assertEquals(1, actualFileEntities.size());
        verify(fileEntityRepository, times(1)).findAllByDocumentId(anyInt());
    }

    @Test
    @DisplayName("5. Testing the save of fileEntity. OK.")
    @Order(5)
    void saveOk() {
        this.fileEntity.setId(0);
        this.fileEntity.setBytes(new byte[]{1, 2, 3});
        this.fileEntity.setFileSize(this.fileEntity.getBytes().length);

        FileEntity fileEntityWithNonZeroId = new FileEntity();
        fileEntityWithNonZeroId.setId(1);
        fileEntityWithNonZeroId.setFilename("file.pdf");
        fileEntityWithNonZeroId.setContentType("application/pdf");
        fileEntityWithNonZeroId.setDocumentId(1);
        fileEntityWithNonZeroId.setBytes(new byte[]{1, 2, 3});
        fileEntityWithNonZeroId.setFileSize(fileEntityWithNonZeroId.getBytes().length);

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
                () -> assertEquals(fileEntity.getBytes().length, actualFileEntity.getBytes().length)
        );

        verify(fileEntityRepository, times(1)).save(any(FileEntity.class));
        verify(documentRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("6. Testing the save of fileEntity=null. BAD.")
    @Order(6)
    void saveNullBad() {

        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.save(null)
        );
        assertEquals("fileEntity is null", iaeNull.getMessage());
        verify(fileEntityRepository, times(0)).save(any(FileEntity.class));
        verify(documentRepository, times(0)).findById(anyInt());
    }

    @Test
    @DisplayName("7. Testing the save of fileEntity.fileNameIsEmpty. BAD.")
    @Order(7)
    void saveFileNameEmptyBad() {

        this.fileEntity.setFilename(" ");
        this.fileEntity.setBytes(new byte[]{1, 2, 3});

        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.save(this.fileEntity)
        );

        assertEquals("Не верно указаны реквизиты файла filename: " +
                this.fileEntity.getFilename(), iaeNull.getMessage());
        verify(fileEntityRepository, times(0)).save(any(FileEntity.class));
    }

    @Test
    @DisplayName("8. Testing the save of fileEntity. Attached File is Empty. BAD.")
    @Order(8)
    void saveEmptyFileBad() {

        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.save(this.fileEntity)
        );

        assertEquals("Не добавлен файл:" + fileEntity.getFilename(), iaeNull.getMessage());
        verify(fileEntityRepository, times(0)).save(any(FileEntity.class));
    }

    @Test
    @DisplayName("9. Testing the save of fileEntity. The fileSize is incorrect value.")
    @Order(9)
    void saveFileEntityWithOtherFileLength() {

        this.fileEntity.setBytes(new byte[]{1, 2, 3, 4});
        System.out.println("---- До сохранения (this.fileEntity.getFileSize()): " + this.fileEntity.getFileSize());

        when(documentRepository.findById(anyInt())).thenReturn(Optional.of(new Document()));

        fileEntityService.save(this.fileEntity);
        System.out.println("---- После сохранения (this.fileEntity.getFileSize()): " + this.fileEntity.getFileSize());
        assertEquals(4, this.fileEntity.getFileSize());
        verify(fileEntityRepository, times(1)).save(any(FileEntity.class));
        verify(documentRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("10. Testing saving a fileEntity with an invalid document id.")
    @Order(10)
    void saveFileEntityBadDocId(){
        this.fileEntity.setBytes(new byte[]{1, 2, 3, 4});

        when(documentRepository.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException re = assertThrows(RuntimeException.class,
                () -> fileEntityService.save(this.fileEntity)
        );
        assertEquals("Не найден документ с id - " + fileEntity.getDocumentId(), re.getMessage());
        verify(fileEntityRepository, times(0)).save(any(FileEntity.class));
    }

    @Test
    @DisplayName("11. Testing deleting of fileEntity by id. OK.")
    @Order(11)
    void deleteByIdOk() {
        this.fileEntity.setId(1);

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.of(this.fileEntity));

        int deletedId = fileEntityService.deleteById(this.fileEntity.getId());

        assertEquals(this.fileEntity.getId(), deletedId);
        verify(fileEntityRepository, times(1)).findById(anyInt());
        verify(fileEntityRepository, times(1)).deleteById(anyInt());
    }

    @Test
    @DisplayName("12. Testing deleting of fileEntity by bad id. BAD.")
    @Order(12)
    void deleteByIdBAD() {
        this.fileEntity.setId(1);

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException re = assertThrows(RuntimeException.class,
                () -> fileEntityService.deleteById(this.fileEntity.getId())
        );

        assertEquals("Не найден файл (fileEntity) с id - " + fileEntity.getId(), re.getMessage());

        verify(fileEntityRepository, times(1)).findById(anyInt());
        verify(fileEntityRepository, times(0)).deleteById(anyInt());
    }
}
