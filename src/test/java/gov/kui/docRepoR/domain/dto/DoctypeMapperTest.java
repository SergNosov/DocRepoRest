package gov.kui.docRepoR.domain.dto;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.domain.DoctypeRandomFactory;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.mappers.DoctypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoctypeMapperTest {
    private final DoctypeMapper doctypeMapper = DoctypeMapper.INSTANCE;
    private Doctype doctype;
    private DoctypeDto doctypeDto;

    @BeforeEach
    void SetUp() {
        doctype= DoctypeRandomFactory.getRandomDoctype();
        doctypeDto = DoctypeRandomFactory.getRandomDoctypeDto();
    }

    @Test
    void doctypeToDoctypeDtoTest() {
        DoctypeDto doctypeDtoActual = doctypeMapper.doctypeToDoctypeDto(doctype);

        assertNotNull(doctypeDtoActual);
        assertEquals(doctype.getId(), doctypeDtoActual.getId());
        assertEquals(doctype.getTitle(), doctypeDtoActual.getTitle());
    }

    @Test
    void doctypeDtoToDoctype() {
        Doctype doctypeActual = doctypeMapper.doctypeDtoToDoctype(doctypeDto);

        assertNotNull(doctypeActual);
        assertEquals(doctypeDto.getId(), doctypeActual.getId());
        assertEquals(doctypeDto.getTitle(), doctypeActual.getTitle());
    }

    @Test
    void doctypeMapperGetNullTest() {
        DoctypeDto doctypeDtoActual = doctypeMapper.doctypeToDoctypeDto(null);
        assertNull(doctypeDtoActual);

        Doctype doctypeActual = doctypeMapper.doctypeDtoToDoctype(null);
        assertNull(doctypeActual);
    }
}
