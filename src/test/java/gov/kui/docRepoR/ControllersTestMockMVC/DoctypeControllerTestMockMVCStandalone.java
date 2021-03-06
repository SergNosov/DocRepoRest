package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.JsonDoctype;
import gov.kui.docRepoR.controller.DoctypeController;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.facade.DoctypeServiceFacade;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;

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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
public class DoctypeControllerTestMockMVCStandalone {

    @Mocked
    private UniqueValueValidator uniqueValueValidator;

    @Mock
    private DoctypeServiceFacade doctypeFacade;

    @InjectMocks
    private DoctypeController doctypeController;

    @Captor
    ArgumentCaptor<DoctypeDto> captorDoctype = ArgumentCaptor.forClass(DoctypeDto.class);

    private MockMvc mockMvc;
    private DoctypeDto validDoctypeDto;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) throws IOException {

        validDoctypeDto = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDoctype.JSON_GOOD.toString(), DoctypeDto.class);

        mockMvc = MockMvcBuilders.standaloneSetup(doctypeController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void testGetAllDoctypes() throws Exception {

        List<DoctypeDto> doctypes = new ArrayList<>();
        doctypes.add(validDoctypeDto);

        given(doctypeFacade.findAll()).willReturn(doctypes);

        mockMvc.perform(get(DocRepoURL.DOCTYPES_LOCALHOST.toString()).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("api/doctypes-all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetDoctypeById() throws Exception {

        given(doctypeFacade.findById(anyInt())).willReturn(validDoctypeDto);

        mockMvc.perform(get(DocRepoURL.DOCTYPES_LOCALHOST.toString() + "/{id}", validDoctypeDto.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("api/doctypes-get", pathParameters(
                        parameterWithName("id").description("id of doctype to get.")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of doctype"),
                                fieldWithPath("title").description("title of doctype")
                        )))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validDoctypeDto.getId())));
    }

    @Test
    void testAddDoctypeOk() throws Exception {

        new Expectations() {{
            uniqueValueValidator.isValid((String) any, (ConstraintValidatorContext) any);
            result = true;
        }};

        given(doctypeFacade.save(any())).willReturn(validDoctypeDto);

        ConstrainedFields fields = new ConstrainedFields(DoctypeDto.class);

        mockMvc.perform(post(DocRepoURL.DOCTYPES_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDoctype.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(document("api/doctypes-new",
                        requestFields(
                                fields.withPath("id").description("Id of doctype"),
                                fieldWithPath("title").ignored() // т.к. на поле установлена ползовательская аннотация....
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of doctype"),
                                fieldWithPath("title").description("title of doctype")
                        )
                        ));

        then(doctypeFacade).should().save(captorDoctype.capture());
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

        given(doctypeFacade.update(any())).willReturn(validDoctypeDto);

        mockMvc.perform(put(DocRepoURL.DOCTYPES_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDoctype.JSON_GOOD.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    void testDeleteDoctype() throws Exception {
        given(doctypeFacade.deleteById(anyInt())).willReturn(validDoctypeDto.getId());

        mockMvc.perform(delete(DocRepoURL.DOCTYPES_LOCALHOST.toString() + "/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Удален тип документа id - " +
                        validDoctypeDto.getId())));
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}
