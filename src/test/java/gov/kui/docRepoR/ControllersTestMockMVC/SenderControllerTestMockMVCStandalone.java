package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.JsonSender;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.controller.SenderController;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.facade.SenderServiceFacade;
import gov.kui.docRepoR.validation.UniqueValueValidator;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SenderControllerTestMockMVCStandalone {

    @Mocked
    private UniqueValueValidator uniqueValueValidator;

    @Mock
    private SenderServiceFacade senderFacade;

    @InjectMocks
    private SenderController senderController;

    @Captor
    ArgumentCaptor<SenderDto> captorSender = ArgumentCaptor.forClass(SenderDto.class);

    private MockMvc mockMvc;
    private SenderDto validSenderDto;

    @BeforeEach
    void setUp() throws IOException {

        validSenderDto = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonSender.JSON_GOOD.toString(), SenderDto.class);

        mockMvc = MockMvcBuilders.standaloneSetup(senderController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter()).build();
    }

    @Test
    void testGetAllSenders() throws Exception {

        List<SenderDto> senders = new ArrayList<>();
        senders.add(validSenderDto);

        given(senderFacade.findAll()).willReturn(senders);

        mockMvc.perform(get(DocRepoURL.SENDERS_LOCALHOST.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetSenderById() throws Exception {

        given(senderFacade.findById(anyInt())).willReturn(validSenderDto);

        mockMvc.perform(get(DocRepoURL.SENDERS_LOCALHOST.toString() + "/" + validSenderDto.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validSenderDto.getId())));
    }

    @Test
    void testAddSenderOk() throws Exception {

        new Expectations() {{
            uniqueValueValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        given(senderFacade.save(any())).willReturn(validSenderDto);

        mockMvc.perform(post(DocRepoURL.SENDERS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSender.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        then(senderFacade).should().save(captorSender.capture());
        assertEquals(0, captorSender.getValue().getId());
    }

    @Test
    void testAddSenderNullBad() throws Exception {

        mockMvc.perform(post(DocRepoURL.SENDERS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSender.JSON_NULL.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", containsString("Необходимо указать наименование отправителя")));
    }

    @Test
    void testUpdateSenderZeroIdBad() throws Exception {

        new Expectations() {{
            uniqueValueValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        MvcResult result = mockMvc.perform(put(DocRepoURL.SENDERS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSender.JSON_ZERO_ID.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message",
                        containsString("При обновлении(update) id не должно быть равно 0.")))
                .andReturn();

        Optional<IllegalArgumentException> iae = Optional.ofNullable(
                (IllegalArgumentException) result.getResolvedException());

        iae.ifPresent((se) -> assertNotNull(se));
        iae.ifPresent((se) -> assertEquals(se.getClass(), IllegalArgumentException.class));
    }

    @Test
    void testUpdateSenderOK() throws Exception {

        new Expectations() {{
            uniqueValueValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        given(senderFacade.update(any())).willReturn(validSenderDto);

        mockMvc.perform(put(DocRepoURL.SENDERS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSender.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testDeleteSender() throws Exception {
        given(senderFacade.deleteById(anyInt())).willReturn(validSenderDto.getId());

        mockMvc.perform(delete(DocRepoURL.SENDERS_LOCALHOST.toString() + "/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Удален отправитель id - " +
                        validSenderDto.getId())));
    }
}
