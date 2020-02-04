package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.IT.JsonSenders;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.controller.SenderController;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.service.SenderService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SenderControllerTestMockMVCStandalone {

    @Mocked
    private UniqueValueValidator uniqueValueValidator;

    @Mock
    private SenderService senderService;

    @InjectMocks
    private SenderController senderController;

    @Captor
    ArgumentCaptor<Sender> captorSender = ArgumentCaptor.forClass(Sender.class);

    private MockMvc mockMvc;
    private  Sender validSender;

    @BeforeEach
    void setUp() throws IOException {

        validSender = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonSenders.JSON_GOOD.toString(), Sender.class);

        mockMvc = MockMvcBuilders.standaloneSetup(senderController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter()).build();
    }
    @Test
    void testGetAllSenders() throws Exception {

        List<Sender> senders = new ArrayList<>();
        senders.add(validSender);

        given(senderService.findAll()).willReturn(senders);

        mockMvc.perform(get(DocRepoURL.SENDERS_LOCALHOST.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetSenderById() throws Exception {

        given(senderService.findById(anyInt())).willReturn(validSender);

        mockMvc.perform(get(DocRepoURL.SENDERS_LOCALHOST.toString() + "/" + validSender.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validSender.getId())));
    }

    @Test
    void testAddSenderOk() throws Exception {

        new Expectations() {{
            uniqueValueValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        given(senderService.save(any())).willReturn(validSender);

        mockMvc.perform(post(DocRepoURL.SENDERS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSenders.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        then(senderService).should().save(captorSender.capture());
        assertEquals(0, captorSender.getValue().getId());
    }

    @Test
    void testAddSenderNullBad() throws Exception {

        mockMvc.perform(post(DocRepoURL.SENDERS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSenders.JSON_NULL.toString()))
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
                .content(JsonSenders.JSON_ZERO_ID.toString()))
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

        given(senderService.save(any())).willReturn(validSender);

        mockMvc.perform(put(DocRepoURL.SENDERS_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonSenders.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testDeleteSender() throws Exception {
        given(senderService.deleteById(anyInt())).willReturn(validSender.getId());

        mockMvc.perform(delete(DocRepoURL.SENDERS_LOCALHOST.toString() + "/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Удален отправитель id - " +
                        validSender.getId())));
    }

}
