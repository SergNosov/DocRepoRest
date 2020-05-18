package gov.kui.docRepoR.domain.dto;

import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.dto.mappers.SenderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SenderMapperTest {
    private final int id = 1;
    private final String title = "ЗаголовокТипаДокумента";
    private final SenderMapper senderMapper = SenderMapper.INSTANCE;
    private Sender sender;
    private SenderDto senderDto;
    private Set<Sender> senders = new HashSet<>();
    private Set<SenderDto> senderDtos = new HashSet<>();

    @BeforeEach
    void setUp() {
        sender = new Sender();
        sender.setId(id);
        sender.setTitle(title);
        senders.add(sender);

        senderDto = SenderDto.builder().id(id).title(title).build();
        senderDtos.add(senderDto);
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
        assertNull(senderMapper.senderToSenderDto(null));
        assertNull(senderMapper.senderDtoToSender(null));
        assertNull(senderMapper.sendersToSenderDtos(null));
        assertNull(senderMapper.senderDtosToSenders(null));
    }

    @Test
    void sendersToDtos (){
        Set<SenderDto> senderDtosActual = senderMapper.sendersToSenderDtos(senders);

        assertNotNull(senderDtosActual);
        assertEquals(senders.size(),senderDtosActual.size());
    }

    @Test
    void senderDtosToSenders (){
        Set<Sender> sendersActual = senderMapper.senderDtosToSenders(senderDtos);

        assertNotNull(sendersActual);
        assertEquals(senderDtos.size(),sendersActual.size());
    }
}
