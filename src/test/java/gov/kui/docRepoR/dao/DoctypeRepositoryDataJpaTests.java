package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dto.DoctypeDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DoctypeRepositoryDataJpaTests {

    @Autowired
    private DoctypeRepository doctypeRepository;

    private Doctype doctypeActual;

    @BeforeEach
    void setUp(){
        doctypeActual = new Doctype();
        doctypeActual.setTitle("NEW Vegas");
    }

    @Test
    void findDoctypeDtoByIdTest() {
        doctypeActual = doctypeRepository.save(doctypeActual);

        DoctypeDto doctypeDtoActual = doctypeRepository.findDtoById(doctypeActual.getId()).get();

        assertNotNull(doctypeDtoActual);
        assertEquals(doctypeActual.getId(), doctypeDtoActual.getId());
        assertEquals(doctypeActual.getTitle(), doctypeDtoActual.getTitle());

        log.info("--- doctypeActual: " + doctypeActual);
        log.info("--- doctypeDto: " + doctypeDtoActual);
    }

    @Test
    void findDoctypeDtoByIdNotFound() {
        Optional<DoctypeDto> dtoOptional = doctypeRepository.findDtoById(Integer.MIN_VALUE);

        assertNotNull(dtoOptional);
        assertFalse(dtoOptional.isPresent());
    }

    @Test
    void findAllDtosTest() {
        List<DoctypeDto> doctypeDtos = doctypeRepository.findAllDtos();
        assertNotNull(doctypeDtos);
    }
}
