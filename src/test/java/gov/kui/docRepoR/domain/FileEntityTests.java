package gov.kui.docRepoR.domain;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

public class FileEntityTests {

    @Test
    void testCreateEntityOk() {

        MultipartFile multipartFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );

        final int idDoc = 21;

        FileEntity fileEntity = FileEntity.getInstance(multipartFile, idDoc);

        assertAll(
                () -> assertNotNull(fileEntity),
                () -> assertEquals(multipartFile.getName(), fileEntity.getFilename()),
                () -> assertEquals(multipartFile.getContentType(), fileEntity.getContentType()),
                () -> assertEquals(multipartFile.getSize(), fileEntity.getFileSize()),
                () -> assertEquals(multipartFile.getBytes(), fileEntity.getBytes()),
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

        MultipartFile multipartFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{}
        );

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> FileEntity.getInstance(multipartFile, 21)
        );
        assertEquals("Ошибка загрузки файла. File is empty.", iae.getMessage());
    }

    @Test
    void testCreateEntityBadZeroDocId() {

        MultipartFile multipartFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );

        final int idDoc = 0;

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> FileEntity.getInstance(multipartFile, idDoc)
        );
        assertEquals("Ошибка загрузки файла. Document.Id не может быть равен 0.", iae.getMessage());
    }
}
