package gov.kui.docRepoR.domain.dto;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.mappers.FileEntityMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

public class FileEntityMapperTest {

    private final MultipartFile multipartFile = new MockMultipartFile(
            "testFile.pdf",
            "testFile.pdf",
            "application/pdf",
            new byte[]{1, 2, 3}
    );

    private final FileEntityMapper fileEntityMapper = FileEntityMapper.INSTANCE;

    @Test
    void fileEntityToFileEntityDtoTest() {
        FileEntity fileEntity = FileEntity.getInstance(multipartFile, 21);

        FileEntityDto fileEntityDto = fileEntityMapper.fileEntityToFileEntityDto(fileEntity);

        assertAll(
                () -> assertEquals(fileEntity.getId(), fileEntityDto.getId()),
                () -> assertEquals(fileEntity.getFilename(), fileEntityDto.getFilename()),
                () -> assertEquals(fileEntity.getFileSize(), fileEntityDto.getFileSize())
        );
    }

    @Test
    void fileEntityToFileEntityDtoNull() {
        FileEntityDto fileEntityDto = fileEntityMapper.fileEntityToFileEntityDto(null);
        assertNull(fileEntityDto);
    }
}
