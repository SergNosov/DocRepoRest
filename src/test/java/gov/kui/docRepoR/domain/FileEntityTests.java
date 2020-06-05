package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FileEntityTests {
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );
    }

    @Test
    void testCreateEntityOk() throws IOException, SQLException {
        final int idDoc = 21;

        FileEntityBlob fileEntity = FileEntityBlob.getInstance(multipartFile, idDoc);

        assertAll(
                () -> assertNotNull(fileEntity),
                () -> assertEquals(multipartFile.getName(), fileEntity.getFilename()),
                () -> assertEquals(multipartFile.getContentType(), fileEntity.getContentType()),
                () -> assertEquals(multipartFile.getSize(), fileEntity.getFileSize()),
                () -> assertEquals(idDoc, fileEntity.getDocumentId()),
                () -> assertTrue(Arrays.equals(multipartFile.getBytes(), fileEntity.getFileByte()
                        .getBytes(1, (int) fileEntity.getFileByte().length()))
                )
        );
    }

    @Test
    void testCreateEntityBadNull() {
        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> FileEntityBlob.getInstance(null, 21)
        );
        assertEquals("Ошибка загрузки файла. File is null.", iaeNull.getMessage());
    }

    @Test
    void testCreateEntityBadEmpty() {
        MultipartFile zeroByteFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{}
        );

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> FileEntityBlob.getInstance(zeroByteFile, 21)
        );
        assertEquals("Ошибка загрузки файла. File is empty.", iae.getMessage());
    }

    @Test
    void testCreateEntityBadZeroDocId() {
        final int idDoc = 0;

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> FileEntityBlob.getInstance(multipartFile, idDoc)
        );
        assertEquals("Ошибка загрузки файла. Document.Id не может быть равен 0.", iae.getMessage());
    }
}
