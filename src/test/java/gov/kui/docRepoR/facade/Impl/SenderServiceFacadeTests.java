package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.Doctype;
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
    void setUp(){
        validSender = SenderRandomFactory.getRandomSender();
        validSenderDto = SenderRandomFactory.getDtoFromSender(validSender);
    }

    @Test
    @Order(1)
    @DisplayName("1. Testing the receipt of senderDto by id. OK.")
    void findByIdTestOk(){

        given(senderService.findById(anyInt())).willReturn(validSender);
        given(senderMapper.senderToSenderDto(any())).willReturn(validSenderDto);

        SenderDto senderDtoActual = senderServiceFacade.findById(validSender.getId());

        then(senderService).should(times(1)).findById(anyInt());
        then(senderMapper).should(times(1)).senderToSenderDto(any());

        assertNotNull(senderDtoActual);
        assertEquals(validSenderDto.getId(),senderDtoActual.getId());
        assertEquals(validSenderDto.getTitle(),senderDtoActual.getTitle());
    }

    @Test
    @Order(2)
    @DisplayName("2. Testing the receipt of senderDto by id. Not found.")
    void findByIdNotFoundTest(){
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

        RuntimeException rte =  assertThrows(RuntimeException.class,
                ()->senderServiceFacade.findById(validSender.getId())
        );

        then(senderService).should(times(1)).findById(anyInt());
        then(senderMapper).should(times(0)).senderToSenderDto(any());
        assertEquals("sender from senderService is null. id: "+validSender.getId(), rte.getMessage());
    }
}
