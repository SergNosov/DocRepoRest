package gov.kui.docRepoR.service.Impl;

import com.google.common.collect.Lists;
import gov.kui.docRepoR.dao.DocumentRepository;
import gov.kui.docRepoR.dao.FileEntityBlobRepository;
import gov.kui.docRepoR.dao.FileEntityRepository;
import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.FileEntityBlob;
import gov.kui.docRepoR.domain.FileEntityRandomFactory;
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

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private FileEntityBlobRepository fileEntityBlobRepository;

    @InjectMocks
    private FileEntityServiceImpl fileEntityService;

    private FileEntityBlob fileEntityBlob;
    private FileEntityDto fileEntityDto;

    @BeforeEach
    void setUp() {
        fileEntityBlob = FileEntityRandomFactory.getRandomFileEntityBlob(Integer.MIN_VALUE);

        fileEntityDto = FileEntityDto.builder()
                .id(fileEntityBlob.getFileEntity().getId())
                .filename(fileEntityBlob.getFileEntity().getFilename())
                .contentType(fileEntityBlob.getFileEntity().getContentType())
                .fileSize(fileEntityBlob.getFileEntity().getFileSize())
                .build();
    }

    @Test
    @DisplayName("1. Testing the receipt of fileEntityBlob by id. OK.")
    @Order(1)
    void findById() {

        when(fileEntityBlobRepository.findById(anyInt())).thenReturn(Optional.of(this.fileEntityBlob));
        FileEntityBlob actualFileEntityBlob = fileEntityService.findById(Integer.MIN_VALUE);

        verify(fileEntityBlobRepository, times(1)).findById(anyInt());
        assertAll(
                () -> assertNotNull(actualFileEntityBlob),
                () -> assertEquals(fileEntityBlob, actualFileEntityBlob),
                () -> assertEquals(fileEntityBlob.getFileEntity().getFilename(),
                        actualFileEntityBlob.getFileEntity().getFilename()),
                () -> assertEquals(fileEntityBlob.getFileEntity().getContentType(),
                        actualFileEntityBlob.getFileEntity().getContentType()),
                () -> assertEquals(fileEntityBlob.getFileEntity().getFileSize(),
                        actualFileEntityBlob.getFileEntity().getFileSize()),
                () -> assertEquals(fileEntityBlob.getFileEntity().getDocumentId(),
                        actualFileEntityBlob.getFileEntity().getDocumentId()),
                () -> assertEquals(fileEntityBlob.getFileByte().length(),
                        actualFileEntityBlob.getFileByte().length())
        );
    }

    @Test
    @DisplayName("2. Testing the receipt of fileEntityDto by id. OK.")
    @Order(2)
    void findDtoByIdOk() {
        when(fileEntityRepository.findDtoById(anyInt())).thenReturn(Optional.of(fileEntityDto));
        FileEntityDto fileEntityDtoActual = fileEntityService.findDtoById(Integer.MIN_VALUE);

        verify(fileEntityRepository, times(1)).findDtoById(anyInt());
        assertNotNull(fileEntityDtoActual);
        assertEquals(fileEntityDto.getId(), fileEntityDtoActual.getId());
        assertEquals(fileEntityDto.getFilename(), fileEntityDtoActual.getFilename());
        assertEquals(fileEntityDto.getContentType(), fileEntityDtoActual.getContentType());
        assertEquals(fileEntityDto.getFileSize(), fileEntityDtoActual.getFileSize());
    }

    @Test
    @DisplayName("3. Testing the receipt of fileEntityBlob by id. BAD.")
    @Order(3)
    void findByIdNotFound() {

        when(fileEntityBlobRepository.findById(anyInt())).thenReturn(Optional.empty());

        IllegalArgumentException rte = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.findById(Integer.MIN_VALUE));
        assertEquals("Не найден файл (fileEntityBlob) с id - " + Integer.MIN_VALUE, rte.getMessage());
    }

    @Test
    @DisplayName("4. Testing the receipt of fileEntityDto by id. BAD.")
    @Order(4)
    void findDtoByIdNotFound() {
        when(fileEntityRepository.findDtoById(anyInt())).thenReturn(Optional.empty());

        IllegalArgumentException rte = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.findDtoById(Integer.MIN_VALUE));

        assertEquals("Не найден файл (fileEntityDto) с id - " + Integer.MIN_VALUE, rte.getMessage());
    }

    @Test
    @DisplayName("5. Testing the receipt of fileEntityDtos by documentId. OK.")
    @Order(5)
    void findByDocId() {
        List<FileEntityDto> fileEntityDtos = Lists.newArrayList(fileEntityDto);

        when(fileEntityRepository.findFileEntityDtosByDocId((anyInt()))).thenReturn(fileEntityDtos);

        List<FileEntityDto> actualFileEntitieDtos = fileEntityService.findDtosByDocId(Integer.MIN_VALUE);

        verify(fileEntityRepository, times(1)).findFileEntityDtosByDocId(anyInt());
        assertFalse(actualFileEntitieDtos.isEmpty());
    }

    @Test
    @DisplayName("6. Testing the save of fileEntity. OK.")
    @Order(6)
    void saveOk() {

        when(documentRepository.existsById(anyInt())).thenReturn(true);
        when(fileEntityBlobRepository.save(any(FileEntityBlob.class))).thenReturn(fileEntityBlob);

        FileEntity actualFileEntity = fileEntityService.save(fileEntityBlob);

        verify(fileEntityBlobRepository, times(1)).save(any(FileEntityBlob.class));
        verify(documentRepository, times(1)).existsById(anyInt());

        assertAll(
                () -> assertNotNull(actualFileEntity),
                () -> assertEquals(fileEntityBlob.getId(), actualFileEntity.getId()),
                () -> assertEquals(fileEntityBlob.getFileEntity().getFilename(), actualFileEntity.getFilename()),
                () -> assertEquals(fileEntityBlob.getFileEntity().getContentType(), actualFileEntity.getContentType()),
                () -> assertEquals(fileEntityBlob.getFileEntity().getFileSize(), actualFileEntity.getFileSize()),
                () -> assertEquals(fileEntityBlob.getFileEntity().getDocumentId(), actualFileEntity.getDocumentId())
        );
    }

    @Test
    @DisplayName("7. Testing the save of fileEntityBlob=null. BAD.")
    @Order(7)
    void saveNullBad() {

        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.save(null)
        );
        assertEquals("fileEntityBlob is null", iaeNull.getMessage());
        verify(fileEntityBlobRepository, times(0)).save(any(FileEntityBlob.class));
        verify(documentRepository, times(0)).existsById(anyInt());
    }

    @Test
    @DisplayName("8 Testing the save of fileEntityBlob.fileEntity.fileNameIsEmpty. BAD.")
    @Order(8)
    void saveFileNameEmptyBad() {

        fileEntityBlob.getFileEntity().setFilename(" ");

        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.save(fileEntityBlob)
        );

        assertEquals("Не верно указаны реквизиты файла filename: " +
                fileEntityBlob.getFileEntity().getFilename(), iaeNull.getMessage());
        verify(fileEntityBlobRepository, times(0)).save(any(FileEntityBlob.class));
    }

    @Test
    @DisplayName("9. Testing the save of fileEntityBlob. Attached File is Empty. BAD.")
    @Order(9)
    void saveEmptyFileBad() {

        fileEntityBlob.setFileByte(null);
        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> fileEntityService.save(fileEntityBlob)
        );

        assertEquals("fileEntityBlob.getFileByte() is null", iaeNull.getMessage());
        verify(fileEntityBlobRepository, times(0)).save(any(FileEntityBlob.class));
    }

    @Test
    @DisplayName("10. Testing the save of fileEntityBlob. The fileSize is incorrect value.")
    @Order(10)
    void saveFileEntityWithOtherFileLength() throws SQLException {

        fileEntityBlob.getFileEntity().setFileSize(0);
        System.out.println("---- До сохранения (this.fileEntityBlob.getFileEntity.getFileSize()): " +
                fileEntityBlob.getFileEntity().getFileSize());

        when(documentRepository.existsById(anyInt())).thenReturn(true);
        when(fileEntityBlobRepository.save(any(FileEntityBlob.class))).thenReturn(fileEntityBlob);

        fileEntityService.save(fileEntityBlob);
        System.out.println("---- После сохранения (this.fileEntityBlob.getFileEntity.getFileSize()): " +
                fileEntityBlob.getFileEntity().getFileSize());

        assertEquals(3, fileEntityBlob.getFileEntity().getFileSize());
        verify(fileEntityBlobRepository, times(1)).save(any(FileEntityBlob.class));
        verify(documentRepository, times(1)).existsById(anyInt());
    }

    @Test
    @DisplayName("11. Testing saving a fileEntityBlob.fileEntity with an invalid document id.")
    @Order(11)
    void saveFileEntityBadDocId() {

        when(documentRepository.existsById(anyInt())).thenReturn(false);

        RuntimeException re = assertThrows(RuntimeException.class,
                () -> fileEntityService.save(fileEntityBlob)
        );
        assertEquals("Не найден документ с id - " + fileEntityBlob.getFileEntity().getDocumentId(), re.getMessage());
        verify(fileEntityBlobRepository, times(0)).save(any(FileEntityBlob.class));
    }

    @Test
    @DisplayName("12. Testing deleting of fileEntityBlob by id. OK.")
    @Order(12)
    void deleteByIdOk() {

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.of(fileEntityBlob.getFileEntity()));
        when(fileEntityBlobRepository.findById(anyInt())).thenReturn(Optional.of(fileEntityBlob));

        int deletedId = fileEntityService.deleteById(fileEntityBlob.getId());

        assertEquals(fileEntityBlob.getId(), deletedId);
        verify(fileEntityBlobRepository, times(1)).findById(anyInt());
        verify(fileEntityRepository, times(1)).findById(anyInt());
        verify(fileEntityBlobRepository, times(1)).delete(fileEntityBlob);
        verify(fileEntityRepository, times(1)).delete(fileEntityBlob.getFileEntity());
    }

    @Test
    @DisplayName("13. Testing deleting of fileEntityBlob by bad id. BAD.")
    @Order(13)
    void deleteByIdBAD() {

        when(fileEntityRepository.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException re = assertThrows(RuntimeException.class,
                () -> fileEntityService.deleteById(fileEntityBlob.getId())
        );

        assertEquals("Не найден файл (fileEntity) с id - " + fileEntityBlob.getId(), re.getMessage());

        verify(fileEntityRepository, times(1)).findById(anyInt());
        verify(fileEntityBlobRepository, times(0)).findById(anyInt());
        verify(fileEntityBlobRepository, times(0)).delete(fileEntityBlob);
        verify(fileEntityRepository, times(0)).delete(fileEntityBlob.getFileEntity());
    }

    @Test
    @DisplayName("14. Testing saving the fileEntityBlob with zero blob field. BAD.")
    @Order(14)
    void saveFileEntityBlobZero(){
        fileEntityBlob.setFileByte(new Blob() {
            @Override
            public long length() throws SQLException {
                return 0;
            }

            @Override
            public byte[] getBytes(long pos, int length) throws SQLException {
                return new byte[0];
            }

            @Override
            public InputStream getBinaryStream() throws SQLException {
                return null;
            }

            @Override
            public long position(byte[] pattern, long start) throws SQLException {
                return 0;
            }

            @Override
            public long position(Blob pattern, long start) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long pos, byte[] bytes) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
                return 0;
            }

            @Override
            public OutputStream setBinaryStream(long pos) throws SQLException {
                return null;
            }

            @Override
            public void truncate(long len) throws SQLException {

            }

            @Override
            public void free() throws SQLException {

            }

            @Override
            public InputStream getBinaryStream(long pos, long length) throws SQLException {
                return null;
            }
        });

        when(documentRepository.existsById(anyInt())).thenReturn(true);

        RuntimeException re = assertThrows(RuntimeException.class,
                () -> fileEntityService.save(fileEntityBlob)
        );

        assertEquals("Ошибка при сохранении в базу данных. Размер файла равен 0. " +
                fileEntityBlob.getFileEntity().getFilename(), re.getMessage());
        verify(documentRepository,times(1)).existsById(anyInt());
        verify(fileEntityBlobRepository,times(0)).save(any(FileEntityBlob.class));
    }

}
