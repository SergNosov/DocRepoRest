package gov.kui.docRepoR.facade.Impl;

import com.google.common.collect.Lists;
import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.domain.DoctypeRandomFactory;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.mappers.DoctypeMapper;
import gov.kui.docRepoR.service.DoctypeService;
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
public class DoctypeServiceFacadeTests {

    @Mock
    private DoctypeService doctypeService;

    @Mock
    private DoctypeMapper doctypeMapper;

    @InjectMocks
    private DoctypeServiceFacadeImpl doctypeServiceFacade;

    private Doctype validDoctype;
    private DoctypeDto validDoctypeDto;

    @BeforeEach
    void setUp() {
        validDoctype = DoctypeRandomFactory.getRandomDoctype();
        validDoctypeDto = DoctypeRandomFactory.getDtoFromDoctype(validDoctype);
    }

    @Test
    @Order(1)
    @DisplayName("1. Testing the receipt of doctypeDto by id. OK.")
    void findByIdTestOk() {
        given(doctypeService.findDtoById(anyInt())).willReturn(validDoctypeDto);

        DoctypeDto doctypeDtoActual = doctypeServiceFacade.findById(validDoctypeDto.getId());

        then(doctypeService).should(times(1)).findDtoById(anyInt());
        assertNotNull(doctypeDtoActual);
        assertEquals(validDoctypeDto.getId(), doctypeDtoActual.getId());
        assertEquals(validDoctypeDto.getTitle(), doctypeDtoActual.getTitle());
    }

    @Test
    @Order(2)
    @DisplayName("2. Testing the receipt of doctypeDto by id. Not found.")
    void findByIdNotFoundTest() {
        given(doctypeService.findDtoById(anyInt()))
                .willThrow(new IllegalArgumentException("Не найден тип документа с id - " + validDoctypeDto.getId()));

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> doctypeServiceFacade.findById(validDoctypeDto.getId())
        );

        then(doctypeService).should(times(1)).findDtoById(anyInt());
        assertEquals("Не найден тип документа с id - " + validDoctypeDto.getId(), iae.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("3. Testing the receipt of all doctypeDtos. Ok")
    void findAllTest() {
        List<DoctypeDto> doctypeDtos = Lists.newArrayList(validDoctypeDto);

        given(doctypeService.findAllDtos()).willReturn(doctypeDtos);

        List<DoctypeDto> doctypeDtosActual = doctypeServiceFacade.findAll();

        then(doctypeService).should(times(1)).findAllDtos();
        assertNotNull(doctypeDtosActual);
        assertEquals(doctypeDtos.size(), doctypeDtosActual.size());
    }

    @Test
    @Order(4)
    @DisplayName("4. Testing the save new doctypeDto. Ok")
    void saveDoctypeDtoTest() {
        given(doctypeMapper.doctypeDtoToDoctype(any(DoctypeDto.class))).willReturn(validDoctype);
        given(doctypeService.save(any(Doctype.class))).willReturn(validDoctype);
        given(doctypeMapper.doctypeToDoctypeDto(any(Doctype.class))).willReturn(validDoctypeDto);

        DoctypeDto doctypeDtoActual = doctypeServiceFacade.save(validDoctypeDto);

        DoctypeDto doctypeDtoZeroId = new DoctypeDto(0, validDoctypeDto.getTitle());

        then(doctypeMapper).should(times(1)).doctypeDtoToDoctype(doctypeDtoZeroId);
        then(doctypeService).should(times(1)).save(validDoctype);
        then(doctypeMapper).should(times(1)).doctypeToDoctypeDto(validDoctype);

        assertNotNull(doctypeDtoActual);
        assertEquals(validDoctypeDto.getTitle(), doctypeDtoActual.getTitle());
    }

    @Test
    @Order(5)
    @DisplayName("5. Testing the save null doctypeDto. Bad.")
    void saveDoctypeDtoNull() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> doctypeServiceFacade.save(null)
        );

        then(doctypeMapper).should(times(0)).doctypeDtoToDoctype(any());
        then(doctypeService).should(times(0)).save(any());
        then(doctypeMapper).should(times(0)).doctypeToDoctypeDto(any());
        assertEquals("Не указан doctypeDto (null)", iae.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("6. Testing the update new doctypeDto. Ok")
    void updateDoctypeDto() {
        given(doctypeMapper.doctypeDtoToDoctype(any(DoctypeDto.class))).willReturn(validDoctype);
        given(doctypeService.save(any(Doctype.class))).willReturn(validDoctype);
        given(doctypeMapper.doctypeToDoctypeDto(any(Doctype.class))).willReturn(validDoctypeDto);

        DoctypeDto doctypeDtoActual = doctypeServiceFacade.update(validDoctypeDto);

        then(doctypeMapper).should(times(1)).doctypeDtoToDoctype(validDoctypeDto);
        then(doctypeService).should(times(1)).save(validDoctype);
        then(doctypeMapper).should(times(1)).doctypeToDoctypeDto(validDoctype);

        assertNotNull(doctypeDtoActual);
        assertEquals(validDoctypeDto.getTitle(), doctypeDtoActual.getTitle());
    }

    @Test
    @Order(7)
    @DisplayName("7. Testing the update null doctypeDto. Bad.")
    void updateDoctypeDtoNull() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> doctypeServiceFacade.update(null)
        );

        then(doctypeMapper).should(times(0)).doctypeDtoToDoctype(any());
        then(doctypeService).should(times(0)).save(any());
        then(doctypeMapper).should(times(0)).doctypeToDoctypeDto(any());
        assertEquals("Не указан doctypeDto (null)", iae.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("8. Test delete of doctypeDto by id. Ok.")
    void deleteByIdTest() {
        given(doctypeService.deleteById(anyInt())).willReturn(validDoctypeDto.getId());

        int deletedId = doctypeServiceFacade.deleteById(validDoctypeDto.getId());

        then(doctypeService).should(times(1)).deleteById(validDoctypeDto.getId());
        assertEquals(validDoctypeDto.getId(), deletedId);
    }
}
