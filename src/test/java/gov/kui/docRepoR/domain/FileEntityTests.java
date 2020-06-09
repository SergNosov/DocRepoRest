package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;
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

        FileEntityBlob fileEntityBlob = FileEntityBlob.getInstance(multipartFile, idDoc);

        assertAll(
                () -> assertNotNull(fileEntityBlob),
                () -> assertEquals(multipartFile.getName(), fileEntityBlob.getFileEntity().getFilename()),
                () -> assertEquals(multipartFile.getContentType(), fileEntityBlob.getFileEntity().getContentType()),
                () -> assertEquals(multipartFile.getSize(), fileEntityBlob.getFileEntity().getFileSize()),
                () -> assertEquals(idDoc, fileEntityBlob.getFileEntity().getDocumentId())
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
