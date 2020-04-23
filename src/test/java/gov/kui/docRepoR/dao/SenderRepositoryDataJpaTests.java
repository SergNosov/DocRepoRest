package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.SenderDto;
import lombok.extern.slf4j.Slf4j;
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
public class SenderRepositoryDataJpaTests {
    @Autowired
    private SenderRepository senderRepository;

    @Test
    void findDtoByIdTest() {
        Sender sender = new Sender();
        sender.setTitle("Bugs Bunny");

        sender = senderRepository.save(sender);

        SenderDto senderDtoActual = senderRepository.findDtoById(sender.getId()).get();

        assertNotNull(senderDtoActual);
        assertEquals(sender.getId(), senderDtoActual.getId());
        assertEquals(sender.getTitle(), senderDtoActual.getTitle());

        log.info("--- doctype: " + sender);
        log.info("--- doctypeDto: " + senderDtoActual);
    }

    @Test
    void findDoctypeDtoByIdNotFound() {
        Optional<SenderDto> dtoOptional = senderRepository.findDtoById(Integer.MIN_VALUE);

        assertNotNull(dtoOptional);
        assertFalse(dtoOptional.isPresent());
    }

    @Test
    void findAllDtosTest() {
        List<SenderDto> senderDtos = senderRepository.findAllDtos();
        assertNotNull(senderDtos);
    }
}
