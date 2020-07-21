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
    @DisplayName("1. Testing the receipt of senderDto by id. Ok.")
    void findByIdTestOk() {
        given(senderService.findDtoById(anyInt())).willReturn(validSenderDto);

        SenderDto senderDtoActual = senderServiceFacade.findById(validSenderDto.getId());
        then(senderService).should(times(1)).findDtoById(anyInt());

        assertNotNull(senderDtoActual);
        assertEquals(validSenderDto.getId(), senderDtoActual.getId());
        assertEquals(validSenderDto.getTitle(), senderDtoActual.getTitle());
    }

    @Test
    @Order(2)
    @DisplayName("2. Testing the receipt of senderDto by id. Not found.")
    void findByIdNotFoundTest() {
        given(senderService.findDtoById(anyInt())).willThrow(
                new IllegalArgumentException("Не найден отправитель с id - " + validSenderDto.getId())
        );

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> senderServiceFacade.findById(validSenderDto.getId())
        );

        then(senderService).should(times(1)).findDtoById(anyInt());
        assertEquals("Не найден отправитель с id - " + validSenderDto.getId(), iae.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("3. Testing the receipt of all senderDtos. Ok.")
    void findAllTestOk() {
        List<SenderDto> senderDtos = Lists.newArrayList(validSenderDto);

        given(senderService.findAllDtos()).willReturn(senderDtos);

        List<SenderDto> senderDtosActual = senderServiceFacade.findAll();

        then(senderService).should(times(1)).findAllDtos();
        assertNotNull(senderDtosActual);
        assertFalse(senderDtosActual.isEmpty());
        assertEquals(senderDtos.size(),senderDtosActual.size());
    }

    @Test
    @Order(4)
    @DisplayName("4. Testing the save new senderDto. Ok.")
    void saveSenderDtoTest() {
        given(senderMapper.senderDtoToSender(any(SenderDto.class))).willReturn(validSender);
        given(senderService.save(any(Sender.class))).willReturn(validSender);
        given(senderMapper.senderToSenderDto(any(Sender.class))).willReturn(validSenderDto);

        SenderDto senderDtoActual = senderServiceFacade.save(validSenderDto);

        then(senderMapper).should(times(1)).senderDtoToSender(validSenderDto);
        then(senderService).should(times(1)).save(validSender);
        then(senderMapper).should(times(1)).senderToSenderDto(validSender);

        assertNotNull(senderDtoActual);
        assertEquals(validSenderDto.getTitle(), senderDtoActual.getTitle());
    }

    @Test
    @Order(5)
    @DisplayName("5. Testing the save null senderDto. Bad.")
    void saveSenderDtoNullTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> senderServiceFacade.save(null)
        );

        then(senderMapper).should(times(0)).senderDtoToSender(any());
        then(senderService).should(times(0)).save(any());
        then(senderMapper).should(times(0)).senderToSenderDto(any());
        assertEquals("Не указан senderDto (null)", iae.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("6. Testing the update senderDto. Ok")
    void updateSenderDtoTest() {
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
    @Order(7)
    @DisplayName("7. Testing the update null senderDto. Bad.")
    void updateSenderDtoNullTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> senderServiceFacade.update(null)
        );

        then(senderMapper).should(times(0)).senderDtoToSender(any());
        then(senderService).should(times(0)).save(any());
        then(senderMapper).should(times(0)).senderToSenderDto(any());
        assertEquals("Не указан senderDto (null)", iae.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("8. Test delete of senderDto by id. Ok.")
    void deleteByIdTest() {
        given(senderService.deleteById(anyInt())).willReturn(validSenderDto.getId());

        int deletedId = senderServiceFacade.deleteById(validSenderDto.getId());

        then(senderService).should(times(1)).deleteById(validSenderDto.getId());
        assertEquals(validSenderDto.getId(), deletedId);
    }
}
