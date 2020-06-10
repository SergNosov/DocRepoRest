package gov.kui.docRepoR.domain.dto;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.domain.FileEntityBlob;
import gov.kui.docRepoR.dto.FileEntityDto;
import gov.kui.docRepoR.dto.mappers.FileEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FileEntityMapperTest {
    private final FileEntityMapper fileEntityMapper = FileEntityMapper.INSTANCE;
    private MultipartFile multipartFile;
    private FileEntity fileEntity;
    private FileEntityDto fileEntityDto;
    private final Set<FileEntity> fileEntities = new HashSet<>();
    private final Set<FileEntityDto> fileEntityDtos = new HashSet<>();

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );

        fileEntity = FileEntityBlob.getInstance(multipartFile, 21).getFileEntity();
        fileEntityDto = FileEntityDto.builder()
                .id(2)
                .filename(fileEntity.getFilename())
                .contentType(fileEntity.getContentType())
                .fileSize(fileEntity.getFileSize())
                .build();
    }

    @Test
    void fileEntityToFileEntityDtoTest() {
        FileEntityDto fileEntityDtoActual = fileEntityMapper.fileEntityToFileEntityDto(fileEntity);

        assertAll(
                () -> assertEquals(fileEntity.getId(), fileEntityDtoActual.getId()),
                () -> assertEquals(fileEntity.getFilename(), fileEntityDtoActual.getFilename()),
                () -> assertEquals(fileEntity.getContentType(), fileEntityDtoActual.getContentType()),
                () -> assertEquals(fileEntity.getFileSize(), fileEntityDtoActual.getFileSize())
        );
    }

    @Test
    void fileEntityDtoToFileEntity(){
        FileEntity fileEntityActual = fileEntityMapper.fileEntityDtoToFileEntity(fileEntityDto);

        assertAll(
                () -> assertEquals(fileEntityDto.getId(), fileEntityActual.getId()),
                () -> assertEquals(fileEntityDto.getFilename(), fileEntityActual.getFilename()),
                () -> assertEquals(fileEntityDto.getContentType(), fileEntityActual.getContentType()),
                () -> assertEquals(fileEntityDto.getFileSize(), fileEntityActual.getFileSize())
        );
    }

    @Test
    void fileEntitysToFileEntityDtosTest(){
        Set<FileEntityDto> fileEntityDtosActual = fileEntityMapper.fileEntitiesToFileEntityDtos(fileEntities);

        assertNotNull(fileEntityDtosActual);
        assertEquals(fileEntities.size(),fileEntityDtosActual.size());
    }

    @Test
    void fileEntityDtosToFileEntitysTest(){
        Set<FileEntity> fileEntitysActual = fileEntityMapper.fileEntityDtosToFileEntities(fileEntityDtos);

        assertNotNull(fileEntitysActual);
        assertEquals(fileEntities.size(),fileEntitysActual.size());
    }


    @Test
    void fileEntityMapperGetNull() {
        assertNull(fileEntityMapper.fileEntityToFileEntityDto(null));
        assertNull(fileEntityMapper.fileEntityDtoToFileEntity(null));
        assertNull(fileEntityMapper.fileEntitiesToFileEntityDtos(null));
        assertNull(fileEntityMapper.fileEntityDtosToFileEntities(null));
    }
}
