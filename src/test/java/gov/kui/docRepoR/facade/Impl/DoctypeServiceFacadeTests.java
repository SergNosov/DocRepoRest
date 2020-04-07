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
        given(doctypeService.findById(anyInt())).willReturn(validDoctype);
        given(doctypeMapper.doctypeToDoctypeDto(any(Doctype.class))).willReturn(validDoctypeDto);

        DoctypeDto doctypeDtoActual = doctypeServiceFacade.findById(validDoctype.getId());

        then(doctypeService).should(times(1)).findById(anyInt());
        then(doctypeMapper).should(times(1)).doctypeToDoctypeDto(any(Doctype.class));
        assertNotNull(doctypeDtoActual);
        assertEquals(validDoctype.getId(), validDoctypeDto.getId());
        assertEquals(validDoctype.getTitle(), validDoctypeDto.getTitle());
    }

    @Test
    @Order(2)
    @DisplayName("2. Testing the receipt of doctypeDto by id. Not found.")
    void findByIdNotFoundTest() {
        given(doctypeService.findById(anyInt()))
                .willThrow(new IllegalArgumentException("Не найден тип документа с id - " + validDoctype.getId()));

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> doctypeServiceFacade.findById(validDoctype.getId())
        );

        then(doctypeService).should(times(1)).findById(anyInt());
        then(doctypeMapper).should(times(0)).doctypeToDoctypeDto(any(Doctype.class));
        assertEquals("Не найден тип документа с id - " + validDoctype.getId(), iae.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("3. Testing the receipt of doctypeDto by id. Null from service.")
    void findByIdNullTest() {
        given(doctypeService.findById(anyInt())).willReturn(null);

        RuntimeException rte = assertThrows(RuntimeException.class,
                () -> doctypeServiceFacade.findById(validDoctype.getId())
        );

        then(doctypeService).should(times(1)).findById(anyInt());
        then(doctypeMapper).should(times(1)).doctypeToDoctypeDto(null);
        assertEquals("Для указанного значения id: " + validDoctype.getId() + ". doctypeDto = null;", rte.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("4. Testing the receipt of all doctypeDtos. Ok")
    void findAllTest() {
        List<Doctype> doctypes = Lists.newArrayList(validDoctype);
        List<DoctypeDto> doctypeDtos = DoctypeRandomFactory.getDtosFromDoctypes(doctypes);

        given(doctypeService.findAll()).willReturn(doctypes);
        given(doctypeMapper.doctypesToDoctypeDtos(doctypes)).willReturn(doctypeDtos);

        List<DoctypeDto> doctypeDtosActual = doctypeServiceFacade.findAll();

        then(doctypeService).should(times(1)).findAll();
        then(doctypeMapper).should(times(1)).doctypesToDoctypeDtos(doctypes);
        assertNotNull(doctypeDtosActual);
        assertEquals(doctypes.size(), doctypeDtosActual.size());
    }
}
