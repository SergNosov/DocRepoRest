package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.JsonDoctype;
import gov.kui.docRepoR.controller.DoctypeController;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.service.DoctypeService;
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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class DoctypeControllerTestMockMVCStandalone {

    @Mocked
    private UniqueValueValidator uniqueValueValidator;

    @Mock
    private DoctypeService doctypeService;

    @InjectMocks
    private DoctypeController doctypeController;

    @Captor
    ArgumentCaptor<Doctype> captorDoctype = ArgumentCaptor.forClass(Doctype.class);

    private MockMvc mockMvc;
    private Doctype validDoctype;

    @BeforeEach
    void setUp() throws IOException {

        validDoctype = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDoctype.JSON_GOOD.toString(), Doctype.class);

        mockMvc = MockMvcBuilders.standaloneSetup(doctypeController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter()).build();
    }

    @Test
    void testGetAllDoctypes() throws Exception {

        List<Doctype> doctypes = new ArrayList<>();
        doctypes.add(validDoctype);

        given(doctypeService.findAll()).willReturn(doctypes);

        mockMvc.perform(get(DocRepoURL.DOCTYPES_LOCALHOST.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetDoctypeById() throws Exception {

        given(doctypeService.findById(anyInt())).willReturn(validDoctype);

        mockMvc.perform(get(DocRepoURL.DOCTYPES_LOCALHOST.toString() + "/" + validDoctype.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validDoctype.getId())));
    }

    @Test
    void testAddDoctypeOk() throws Exception {

        new Expectations() {{
            uniqueValueValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        given(doctypeService.save(any())).willReturn(validDoctype);

        mockMvc.perform(post(DocRepoURL.DOCTYPES_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDoctype.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        then(doctypeService).should().save(captorDoctype.capture());
        assertEquals(0, captorDoctype.getValue().getId());
    }

    @Test
    void testAddDoctypeNullBad() throws Exception {

        mockMvc.perform(post(DocRepoURL.DOCTYPES_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDoctype.JSON_NULL.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", containsString("Необходимо указать тип документа")));
    }

    @Test
    void testUpdateDoctypeZeroIdBad() throws Exception {

        new Expectations() {{
            uniqueValueValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        MvcResult result = mockMvc.perform(put(DocRepoURL.DOCTYPES_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDoctype.JSON_ZERO_ID.toString()))
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
    void testUpdateDoctypeOK() throws Exception {

        new Expectations() {{
            uniqueValueValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        given(doctypeService.save(any())).willReturn(validDoctype);

        mockMvc.perform(put(DocRepoURL.DOCTYPES_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDoctype.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testDeleteDoctype() throws Exception {
        given(doctypeService.deleteById(anyInt())).willReturn(validDoctype.getId());

        mockMvc.perform(delete(DocRepoURL.DOCTYPES_LOCALHOST.toString() + "/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Удален тип документа id - " +
                        validDoctype.getId())));
    }
}