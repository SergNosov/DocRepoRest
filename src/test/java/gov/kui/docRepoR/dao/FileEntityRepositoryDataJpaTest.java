package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.FileEntity;
import gov.kui.docRepoR.dto.FileEntityDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FileEntityRepositoryDataJpaTest {

    @Autowired
    private FileEntityRepository fileEntityRepository;

    private FileEntity fileEntity;

    @BeforeEach
    void SetUp(){
        MultipartFile multipartFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );

        fileEntity = FileEntity.getInstance(multipartFile,Integer.MIN_VALUE);
    }

    @Test
    void findDtoByIdTestOk() {
        fileEntity = fileEntityRepository.save(fileEntity);

        FileEntityDto fileEntityDto= fileEntityRepository.findDtoById(fileEntity.getId()).get();

        assertNotNull(fileEntityDto);
        assertEquals(fileEntity.getId(),fileEntityDto.getId());
        assertEquals(fileEntity.getFilename(),fileEntityDto.getFilename());
        assertEquals(fileEntity.getFileSize(),fileEntityDto.getFileSize());

        log.info("--- fileEntity: "+fileEntity);
        log.info("--- fileEntityDto: "+fileEntityDto);
    }

    @Test
    void findDtoByIdTestNotFound(){
        Optional<FileEntityDto> dtoOptional = fileEntityRepository.findDtoById(Integer.MIN_VALUE);

        assertNotNull( dtoOptional);
        assertFalse(dtoOptional.isPresent());
    }

    @Test
    void findDtosByDocIdOk(){
        fileEntity = fileEntityRepository.save(fileEntity);
        List<FileEntityDto> fileEntityDtos = fileEntityRepository.findFileEntityDtosByDocId(Integer.MIN_VALUE);
        assertNotNull(fileEntityDtos);
        assertFalse(fileEntityDtos.isEmpty());
    }
}
