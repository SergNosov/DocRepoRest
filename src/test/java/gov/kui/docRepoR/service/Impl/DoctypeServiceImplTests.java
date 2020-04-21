package gov.kui.docRepoR.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.JsonDoctype;
import gov.kui.docRepoR.dao.DoctypeRepository;
import gov.kui.docRepoR.dto.DoctypeDto;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class DoctypeServiceImplTests {
    @Mock
    private DoctypeRepository doctypeRepository;

    @InjectMocks
    private DoctypeServiceImpl doctypeService;

    private Doctype validDoctype;
    private DoctypeDto validDto;
    private Doctype invalidDoctype;

    @BeforeEach
    void setUp() throws IOException {
        validDoctype = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDoctype.JSON_GOOD.toString(), Doctype.class);

        validDto = DoctypeDto.builder().id(validDoctype.getId()).title(validDoctype.getTitle()).build();

        invalidDoctype = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(JsonDoctype.JSON_NO_REQURED_FIELDS.toString(), Doctype.class);
    }

    @Test
    @DisplayName("1. Testing the receipt of all doctypes.")
    @Order(1)
    void testGetAllDoctypes() {
        List<Doctype> doctypeList = new ArrayList<>();
        doctypeList.add(validDoctype);

        given(doctypeRepository.findAll()).willReturn(doctypeList);
        List<Doctype> returnedList = doctypeService.findAll();
        then(doctypeRepository).should().findAll();
        assertNotNull(returnedList);
        assertEquals(1, returnedList.size());
    }

    @Test
    @DisplayName("2. Testing the receipt of doctype by id. OK.")
    @Order(2)
    void testGetDoctypeByIdOk() {
        given(doctypeRepository.findById(anyInt())).willReturn(Optional.of(validDoctype));
        Doctype actualDoctype = doctypeService.findById(validDoctype.getId());
        then(doctypeRepository).should().findById(validDoctype.getId());
        assertAll(
                () -> assertNotNull(actualDoctype),
                () -> assertEquals(validDoctype.getId(), actualDoctype.getId()),
                () -> assertEquals(validDoctype.getTitle(), actualDoctype.getTitle())
        );
    }

    @Test
    @DisplayName("3. Testing the receipt of doctype by id. BAD.")
    @Order(3)
    void testGetDoctypeByIdBad() {
        given(doctypeRepository.findById(anyInt())).willReturn(Optional.empty());
        IllegalArgumentException rte = assertThrows(IllegalArgumentException.class,
                () -> doctypeService.findById(validDoctype.getId())
        );
        assertEquals("Не найден тип документа с id - " + validDoctype.getId(), rte.getMessage());
    }

    @Test
    void testGetDoctypeDtoByIdBad(){
        given(doctypeRepository.findDtoById(anyInt())).willReturn(Optional.empty());
        IllegalArgumentException rte = assertThrows(IllegalArgumentException.class,
                () -> doctypeService.findDtoById(validDoctype.getId())
        );
        assertEquals("Не найден тип документа с id - " + validDoctype.getId(), rte.getMessage());
    }

    @Test
    @DisplayName("4. Testing deleteById doctype by id. OK.")
    @Order(4)
    void testDeleteDoctypeByIdOk() {
        given(doctypeRepository.findById(anyInt())).willReturn(Optional.of(validDoctype));
        int deletedId = doctypeService.deleteById(validDoctype.getId());
        assertEquals(validDoctype.getId(), deletedId);
    }

    @Test
    @DisplayName("5. Testing deleteById doctype by id. BAD.")
    @Order(5)
    void testDeleteDoctypeByIdBad() {
        given(doctypeRepository.findById(anyInt())).willReturn(Optional.empty());
        RuntimeException rte = assertThrows(RuntimeException.class, () -> doctypeService.findById(validDoctype.getId()));
        assertEquals("Не найден тип документа с id - " + validDoctype.getId(), rte.getMessage());
    }

    @Test
    @DisplayName("6. Testing save doctype (null). BAD.")
    @Order(6)
    void testSaveDoctypeNull() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> doctypeService.save(null));
        assertEquals("Не указан doctype (null)", iae.getMessage());
        then(doctypeRepository).should(times(0)).save(any());
    }

    @Test
    @DisplayName("7. Testing save doctype. BAD (Title is null)")
    @Order(7)
    void testSaveDoctypeTitleNull() {
        invalidDoctype.setTitle(null);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> doctypeService.save(invalidDoctype));
        assertEquals("Заголовок (doctype.title) пуст. doctype: " + invalidDoctype, iae.getMessage());
        then(doctypeRepository).should(times(0)).save(any());
    }

    @Test
    @DisplayName("8. Testing save doctype. BAD (Title is blank).")
    @Order(8)
    void testSaveDoctypeTitleBlank() {
        validDoctype.setTitle("   ");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> doctypeService.save(validDoctype));
        assertEquals("Заголовок (doctype.title) пуст. doctype: " + validDoctype, iae.getMessage());
        then(doctypeRepository).should(times(0)).save(any());
    }

    @Test
    @DisplayName("9. Testing save doctype. OK.")
    @Order(9)
    void testSaveDoctypeOk() {
        given(doctypeRepository.findById(anyInt())).willReturn(Optional.of(validDoctype));
        given(doctypeRepository.save(any())).willReturn(validDoctype);

        Doctype savedDoctype = doctypeService.save(validDoctype);

        assertAll(
                () -> assertNotNull(savedDoctype),
                () -> assertEquals(validDoctype.getId(), savedDoctype.getId()),
                () -> assertEquals(validDoctype.getTitle(), savedDoctype.getTitle())
        );
    }

    @Test
    @DisplayName("10. Testing of  title is exists(isExists = false).")
    @Order(10)
    void testIsExistsValueInField() {
        given(doctypeRepository.existsByTitle(anyString())).willReturn(false);
        boolean actualExists = doctypeService.isExistsValueInField("Title", "title");
        then(doctypeRepository).should().existsByTitle(anyString());
        assertFalse(actualExists);
    }

    @Test
    @DisplayName("11. Testing of  title is exists(isExists = true).")
    @Order(11)
    void testIsExistsValueInField_True() {
        given(doctypeRepository.existsByTitle(anyString())).willReturn(true);
        boolean actualExists = doctypeService.isExistsValueInField("Title", "title");
        then(doctypeRepository).should().existsByTitle(anyString());
        assertTrue(actualExists);
    }

    @Test
    @DisplayName("12. Testing of  title is exists(value - null).")
    @Order(12)
    void testIsExistsValueInField_Null() {
        boolean actualExists = doctypeService.isExistsValueInField(null, null);
        assertFalse(actualExists);
    }

    @Test
    @DisplayName("13. Testing of  title is exists(illegal field).")
    @Order(13)
    void testIsExistsValueInField_illegalField() {
        String fieldName = "otherField";
        UnsupportedOperationException uoe = assertThrows(UnsupportedOperationException.class,
                () -> doctypeService.isExistsValueInField("title", fieldName));
        assertEquals("Validation on field: '" + fieldName + "' not supported.", uoe.getMessage());
    }

    @Test
    @DisplayName("14. Testing the receipt of all doctypeDtos.")
    @Order(14)
    void testGetAllDoctypeDtos() {
        List<DoctypeDto> doctypeDtos = new ArrayList<>();
        doctypeDtos.add(validDto);

        given(doctypeRepository.findAllDtos()).willReturn(doctypeDtos);
        List<DoctypeDto> doctypeDtosActual = doctypeService.findAllDtos();

        assertNotNull(doctypeDtosActual);
        assertEquals(1, doctypeDtosActual.size());
    }

    @Test
    @DisplayName("15. Testing the receipt of doctypeDto by id. OK.")
    @Order(15)
    void testGetDoctypeDtoById() {
        given(doctypeRepository.findDtoById(anyInt())).willReturn(Optional.of(validDto));
        DoctypeDto actualDoctypeDto = doctypeService.findDtoById(validDto.getId());
        then(doctypeRepository).should(times(1)).findDtoById(validDto.getId());

        assertAll(
                () -> assertNotNull(actualDoctypeDto),
                () -> assertEquals(validDto.getId(), actualDoctypeDto.getId()),
                () -> assertEquals(validDto.getTitle(), actualDoctypeDto.getTitle())
        );
    }
}
