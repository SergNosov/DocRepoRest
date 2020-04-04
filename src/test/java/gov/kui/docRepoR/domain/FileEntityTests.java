package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

public class FileEntityTests {
    public static final MultipartFile multipartFile = new MockMultipartFile(
                "testFile.pdf",
                        "testFile.pdf",
                        "application/pdf",
                        new byte[]{1, 2, 3}
        );

    @Test
    void testCreateEntityOk() {
        final int idDoc = 21;

        FileEntity fileEntity = FileEntity.getInstance(multipartFile, idDoc);

        assertAll(
                () -> assertNotNull(fileEntity),
                () -> assertEquals(multipartFile.getName(), fileEntity.getFilename()),
                () -> assertEquals(multipartFile.getContentType(), fileEntity.getContentType()),
                () -> assertEquals(multipartFile.getSize(), fileEntity.getFileSize()),
                () -> assertEquals(multipartFile.getBytes(), fileEntity.getFileByte().getBytes()),
                () -> assertEquals(idDoc, fileEntity.getDocumentId())
        );
    }

    @Test
    void testCreateEntityBadNull() {
        IllegalArgumentException iaeNull = assertThrows(IllegalArgumentException.class,
                () -> FileEntity.getInstance(null, 21)
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
                () -> FileEntity.getInstance(zeroByteFile, 21)
        );
        assertEquals("Ошибка загрузки файла. File is empty.", iae.getMessage());
    }

    @Test
    void testCreateEntityBadZeroDocId() {
        final int idDoc = 0;

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> FileEntity.getInstance(multipartFile, idDoc)
        );
        assertEquals("Ошибка загрузки файла. Document.Id не может быть равен 0.", iae.getMessage());
    }
}
