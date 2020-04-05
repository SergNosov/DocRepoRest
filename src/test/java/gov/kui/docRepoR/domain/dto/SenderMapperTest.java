package gov.kui.docRepoR.domain.dto;

import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.dto.mappers.SenderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SenderMapperTest {
    private final int id = 1;
    private final String title = "ЗаголовокТипаДокумента";
    private final SenderMapper senderMapper = SenderMapper.INSTANCE;
    private Sender sender;
    private SenderDto senderDto;

    @BeforeEach
    void setUp() {
        sender = new Sender();
        sender.setId(id);
        sender.setTitle(title);

        senderDto = SenderDto.builder().id(id).title(title).build();
    }

    @Test
    void senderToSenderDtoTest() {
        SenderDto senderDtoActual = senderMapper.senderToSenderDto(sender);

        assertEquals(id, senderDtoActual.getId());
        assertEquals(title, senderDtoActual.getTitle());
    }

    @Test
    void senderDtoToSenderTest() {
        Sender senderActual = senderMapper.senderDtoToSender(senderDto);

        assertEquals(id, senderActual.getId());
        assertEquals(title, senderActual.getTitle());
    }

    @Test
    void setSenderMapperGetNull() {
        SenderDto senderDtoActual = senderMapper.senderToSenderDto(null);
        assertNull(senderDtoActual);

        Sender senderActual = senderMapper.senderDtoToSender(null);
        assertNull(senderActual);
    }
}
