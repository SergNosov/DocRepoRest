package gov.kui.docRepoR.domain.dto;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.FileEntityTests;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.mappers.FileEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

public class FileEntityMapperTest {
    private final FileEntityMapper fileEntityMapper = FileEntityMapper.INSTANCE;
    private MultipartFile multipartFile;
    private FileEntity fileEntity;
    private FileEntityDto fileEntityDto;

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );

        fileEntity = FileEntity.getInstance(multipartFile, 21);
        fileEntityDto = FileEntityDto.builder()
                .id(2)
                .filename(fileEntity.getFilename())
                .fileSize(fileEntity.getFileSize())
                .build();
    }

    @Test
    void fileEntityToFileEntityDtoTest() {
        FileEntityDto fileEntityDtoActual = fileEntityMapper.fileEntityToFileEntityDto(fileEntity);

        assertAll(
                () -> assertEquals(fileEntity.getId(), fileEntityDtoActual.getId()),
                () -> assertEquals(fileEntity.getFilename(), fileEntityDtoActual.getFilename()),
                () -> assertEquals(fileEntity.getFileSize(), fileEntityDtoActual.getFileSize())
        );
    }

    @Test
    void fileEntityDtoToFileEntity(){ //todo нужен ли вообще переход из fEDto в fE ?????
        FileEntity fileEntityActual = fileEntityMapper.fileEntityDtoToFileEntity(fileEntityDto);

        assertAll(
                () -> assertEquals(fileEntityDto.getId(), fileEntityActual.getId()),
                () -> assertEquals(fileEntityDto.getFilename(), fileEntityActual.getFilename()),
                () -> assertEquals(fileEntityDto.getFileSize(), fileEntityActual.getFileSize())
        );

    }

    @Test
    void fileEntityMapperGetNull() {
        FileEntityDto fileEntityDtoActual = fileEntityMapper.fileEntityToFileEntityDto(null);
        assertNull(fileEntityDtoActual);

        FileEntity fileEntityActual = fileEntityMapper.fileEntityDtoToFileEntity(null);
        assertNull(fileEntityActual);
    }
}
