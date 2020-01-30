package gov.kui.docRepoR.IT.ControllersTestMockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.IT.JsonDoctypes;
import gov.kui.docRepoR.controller.DoctypeController;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.service.DoctypeService;
import gov.kui.docRepoR.validation.CheckValueIsExists;
import gov.kui.docRepoR.validation.UniqueValue;
import gov.kui.docRepoR.validation.UniqueValueValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class DoctypeControllerTestMockMVCStandalone {

    @Mock
    private CheckValueIsExists checkValueIsExists;

    @Mock
    private UniqueValue uniqueValue;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
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
                .readValue(JsonDoctypes.JSON_GOOD.toString(), Doctype.class);

        mockMvc = MockMvcBuilders.standaloneSetup(doctypeController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter()).build();
    }

    @Test
    void testGetAllDocuments() throws Exception {

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

        given(doctypeService.save(any())).willReturn(validDoctype);
        given(uniqueValueValidator.isValid(any(),any())).willReturn(true);

        mockMvc.perform(post(DocRepoURL.DOCTYPES_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDoctypes.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        then(doctypeService).should().save(captorDoctype.capture());
        assertEquals(0,captorDoctype.getValue().getId());
    }
}
