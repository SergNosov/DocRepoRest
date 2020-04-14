package gov.kui.docRepoR.facade.Impl;

import com.google.common.collect.Lists;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.domain.SenderRandomFactory;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.dto.mappers.SenderMapper;
import gov.kui.docRepoR.service.SenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class SenderServiceFacadeTests {

    @Mock
    private SenderService senderService;
    @Mock
    private SenderMapper senderMapper;
    @InjectMocks
    private SenderServiceFacadeImpl senderServiceFacade;

    private Sender validSender;
    private SenderDto validSenderDto;

    @BeforeEach
    void setUp() {
        validSender = SenderRandomFactory.getRandomSender();
        validSenderDto = SenderRandomFactory.getDtoFromSender(validSender);
    }

    @Test
    @Order(1)
    @DisplayName("1. Testing the receipt of senderDto by id. OK.")
    void findByIdTestOk() {

        given(senderService.findById(anyInt())).willReturn(validSender);
        given(senderMapper.senderToSenderDto(any())).willReturn(validSenderDto);

        SenderDto senderDtoActual = senderServiceFacade.findById(validSender.getId());

        then(senderService).should(times(1)).findById(anyInt());
        then(senderMapper).should(times(1)).senderToSenderDto(any());

        assertNotNull(senderDtoActual);
        assertEquals(validSenderDto.getId(), senderDtoActual.getId());
        assertEquals(validSenderDto.getTitle(), senderDtoActual.getTitle());
    }

    @Test
    @Order(2)
    @DisplayName("2. Testing the receipt of senderDto by id. Not found.")
    void findByIdNotFoundTest() {
        given(senderService.findById(anyInt())).willThrow(
                new IllegalArgumentException("Не найден тип документа с id - " + validSender.getId())
        );

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> senderServiceFacade.findById(validSender.getId())
        );

        then(senderService).should(times(1)).findById(anyInt());
        then(senderMapper).should(times(0)).senderToSenderDto(any(Sender.class));
        assertEquals("Не найден тип документа с id - " + validSender.getId(), iae.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("3. Testing the receipt of senderDto by id. Null from service.")
    void findByIdNullTest() {
        given(senderService.findById(anyInt())).willReturn(null);

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> senderServiceFacade.findById(validSender.getId())
        );

        then(senderService).should(times(1)).findById(anyInt());
        then(senderMapper).should(times(0)).senderToSenderDto(any());
        assertEquals("sender from senderService is null. id: " + validSender.getId(), iae.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("4. Testing the receipt of senderDto by id. Null from mapper.")
    void findByIdNullFromMapper() {
        given(senderService.findById(anyInt())).willReturn(validSender);
        given(senderMapper.senderToSenderDto(any())).willReturn(null);

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> senderServiceFacade.findById(validSender.getId())
        );

        then(senderService).should(times(1)).findById(anyInt());
        then(senderMapper).should(times(1)).senderToSenderDto(any());
        assertEquals("senderDto from senderMapper is null. id: " + validSender.getId(), iae.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("5. Testing the receipt of all senderDtos. Ok")
    void findAllTestOk() {
        List<Sender> senders = Lists.newArrayList(validSender);
        List<SenderDto> senderDtos = SenderRandomFactory.getDtosFromSenders(senders);

        given(senderService.findAll()).willReturn(senders);
        given(senderMapper.sendersToSenderDtos(senders)).willReturn(senderDtos);

        List<SenderDto> senderDtosActual = senderServiceFacade.findAll();

        then(senderService).should(times(1)).findAll();
        then(senderMapper).should(times(1)).sendersToSenderDtos(any());
        assertNotNull(senderDtosActual);
        assertFalse(senderDtosActual.isEmpty());
        assertEquals(senders.size(),senderDtosActual.size());
    }

    @Test
    @Order(6)
    @DisplayName("6. Testing the save new senderDto. Ok")
    void saveSenderDtoTest() {
        given(senderMapper.senderDtoToSender(any(SenderDto.class))).willReturn(validSender);
        given(senderService.save(any(Sender.class))).willReturn(validSender);
        given(senderMapper.senderToSenderDto(any(Sender.class))).willReturn(validSenderDto);

        SenderDto senderDtoActual = senderServiceFacade.save(validSenderDto);

        SenderDto senderDtoZeroId = new SenderDto(0, validSenderDto.getTitle());

        then(senderMapper).should(times(1)).senderDtoToSender(senderDtoZeroId);
        then(senderService).should(times(1)).save(validSender);
        then(senderMapper).should(times(1)).senderToSenderDto(validSender);

        assertNotNull(senderDtoActual);
        assertEquals(validSenderDto.getTitle(), senderDtoActual.getTitle());
    }

    @Test
    @Order(7)
    @DisplayName("7. Testing the save null senderDto. Bad.")
    void saveDoctypeDtoNull() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> senderServiceFacade.save(null)
        );

        then(senderMapper).should(times(0)).senderDtoToSender(any());
        then(senderService).should(times(0)).save(any());
        then(senderMapper).should(times(0)).senderToSenderDto(any());
        assertEquals("Не указан senderDto (null)", iae.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("8. Testing the update new senderDto. Ok")
    void updateDoctypeDto() {
        given(senderMapper.senderDtoToSender(any(SenderDto.class))).willReturn(validSender);
        given(senderService.save(any(Sender.class))).willReturn(validSender);
        given(senderMapper.senderToSenderDto(any(Sender.class))).willReturn(validSenderDto);

        SenderDto senderDtoActual = senderServiceFacade.update(validSenderDto);

        then(senderMapper).should(times(1)).senderDtoToSender(validSenderDto);
        then(senderService).should(times(1)).save(validSender);
        then(senderMapper).should(times(1)).senderToSenderDto(validSender);

        assertNotNull(senderDtoActual);
        assertEquals(validSenderDto.getTitle(), senderDtoActual.getTitle());
    }

    @Test
    @Order(9)
    @DisplayName("9. Testing the update null senderDto. Bad.")
    void updateDoctypeDtoNull() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> senderServiceFacade.update(null)
        );

        then(senderMapper).should(times(0)).senderDtoToSender(any());
        then(senderService).should(times(0)).save(any());
        then(senderMapper).should(times(0)).senderToSenderDto(any());
        assertEquals("Не указан senderDto (null)", iae.getMessage());
    }

    @Test
    @Order(10)
    @DisplayName("10. Test delete of senderDto by id. Ok.")
    void deleteByIdTest() {
        given(senderService.deleteById(anyInt())).willReturn(validSenderDto.getId());

        int deletedId = senderServiceFacade.deleteById(validSenderDto.getId());

        then(senderService).should(times(1)).deleteById(validSenderDto.getId());
        assertEquals(validSenderDto.getId(), deletedId);
    }
}
