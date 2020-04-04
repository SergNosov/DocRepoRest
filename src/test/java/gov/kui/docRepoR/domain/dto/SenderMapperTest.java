package gov.kui.docRepoR.domain.dto;

import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.dto.mappers.SenderMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SenderMapperTest {
    private final int id = 1;
    private final String title = "ЗаголовокТипаДокумента";
    private final SenderMapper senderMapper = SenderMapper.INSTANCE;

    @Test
    void senderToSenderDtoTest(){
        Sender sender = new Sender();
        sender.setId(id);
        sender.setTitle(title);

        SenderDto senderDto = senderMapper.senderToSenderDto(sender);

        assertEquals(id,senderDto.getId());
        assertEquals(title,senderDto.getTitle());
    }

    @Test
    void senderToSenderDtoNull(){
        SenderDto senderDto = senderMapper.senderToSenderDto(null);
        assertNull(senderDto);
    }
}
