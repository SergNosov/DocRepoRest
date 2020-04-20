package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dto.DoctypeDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@EnableTransactionManagement
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DoctypeRepositorySBTests {

    @Autowired
    private DoctypeRepository doctypeRepository;

    @Test
    @Transactional
    void findDoctypeDtoByIdTest() {
        Doctype doctype = new Doctype();
        doctype.setTitle("NEW Vegas");

        doctype = doctypeRepository.save(doctype);

        DoctypeDto doctypeDtoActual = doctypeRepository.findDtoById(doctype.getId());

        assertNotNull(doctypeDtoActual);
        assertEquals(doctype.getId(), doctypeDtoActual.getId());
        assertEquals(doctype.getTitle(), doctypeDtoActual.getTitle());

        log.info("--- doctype: " + doctype);
        log.info("--- doctypeDto: " + doctypeDtoActual);
    }

    @Test
    @Transactional
    void findDoctypeDtoByIdNotFound() {
        EmptyResultDataAccessException erdae = assertThrows(EmptyResultDataAccessException.class,
                () -> doctypeRepository.findDtoById(Integer.MIN_VALUE)
        );
    }

    @Test
    @Transactional
    void findAllDtosTest() {
        List<DoctypeDto> doctypeDtos = doctypeRepository.findAllDtos();
        assertNotNull(doctypeDtos);
    }
}
