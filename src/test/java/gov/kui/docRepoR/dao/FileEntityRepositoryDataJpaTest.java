package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.FileEntityBlob;
import gov.kui.docRepoR.dto.FileEntityDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FileEntityRepositoryDataJpaTest {

    @Autowired
    private FileEntityRepository fileEntityRepository;

    @Autowired
    private FileEntityBlobRepository fileEntityBlobRepository;

    private FileEntityBlob fileEntityBlob;

    @BeforeEach
    void SetUp() {
        MultipartFile multipartFile = new MockMultipartFile(
                "testFile.pdf",
                "testFile.pdf",
                "application/pdf",
                new byte[]{1, 2, 3}
        );
        fileEntityBlob = FileEntityBlob.getInstance(multipartFile, Integer.MIN_VALUE);
    }

    @Test
    void findDtoByIdTestOk() {
        fileEntityBlob = fileEntityBlobRepository.save(fileEntityBlob);

        FileEntityDto fileEntityDto = fileEntityRepository.findDtoById(fileEntityBlob.getId()).get();

        assertNotNull(fileEntityDto);
        assertEquals(fileEntityBlob.getId(), fileEntityDto.getId());
        assertEquals(fileEntityBlob.getFileEntity().getFilename(), fileEntityDto.getFilename());
        assertEquals(fileEntityBlob.getFileEntity().getContentType(), fileEntityDto.getContentType());
        assertEquals(fileEntityBlob.getFileEntity().getFileSize(), fileEntityDto.getFileSize());

        log.info("--- fileEntity: " + fileEntityBlob);
        log.info("--- fileEntityDto: " + fileEntityDto);
    }

    @Test
    void findDtoByIdTestNotFound() {
        Optional<FileEntityDto> dtoOptional = fileEntityRepository.findDtoById(Integer.MIN_VALUE);

        assertNotNull(dtoOptional);
        assertFalse(dtoOptional.isPresent());
    }

    @Test
    void findDtosByDocIdOk() {
        fileEntityBlob = fileEntityBlobRepository.save(fileEntityBlob);
        List<FileEntityDto> fileEntityDtos = fileEntityRepository.findFileEntityDtosByDocId(Integer.MIN_VALUE);
        assertNotNull(fileEntityDtos);
        assertFalse(fileEntityDtos.isEmpty());
    }

    @Test
    void testSaveFileEntityBlobOk() throws SQLException {
        fileEntityBlob = fileEntityBlobRepository.save(fileEntityBlob);

        System.out.println("--- fileEntity: " + fileEntityBlob.getFileEntity());
        System.out.println("--- fileEntityBlob: " + fileEntityBlob);
        System.out.println("--- fileEntityBlob: " + Arrays.toString(fileEntityBlob.getFileByte().getBytes(1, (int) fileEntityBlob.getFileByte().length())));

        assertEquals(fileEntityBlob.getFileEntity().getId(), fileEntityBlob.getId());
    }

    @Test
    void testDeleteFileEntityBlob() {
        fileEntityBlob = fileEntityBlobRepository.save(fileEntityBlob);
        fileEntityBlobRepository.deleteById(fileEntityBlob.getId());
        fileEntityRepository.deleteById(fileEntityBlob.getFileEntity().getId());
        assertFalse(fileEntityBlobRepository.existsById(fileEntityBlob.getId()));
        assertFalse(fileEntityRepository.existsById(fileEntityBlob.getFileEntity().getId()));
    }
}
