package gov.kui.docRepoR.domain.dto;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.mappers.DoctypeMapper;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Log
public class DoctypeMapperTest {
    private final int id = 1;
    private final String title = "ЗаголовокТипаДокумента";
    private final DoctypeMapper doctypeMapper = DoctypeMapper.INSTANCE;
    private Doctype doctype;
    private DoctypeDto doctypeDto;

    @BeforeEach
    void SetUp() {
        doctype = new Doctype();
        doctype.setId(id);
        doctype.setTitle(title);

        doctypeDto = DoctypeDto.builder().id(id).title(title).build();
    }

    @Test
    void doctypeToDoctypeDtoTest() {
        DoctypeDto doctypeDtoActual = doctypeMapper.doctypeToDoctypeDto(doctype);

        assertEquals(id, doctypeDtoActual.getId());
        assertEquals(title, doctypeDtoActual.getTitle());
    }

    @Test
    void doctypeDtoToDoctype() {
        Doctype doctypeActual = doctypeMapper.doctypeDtoToDoctype(doctypeDto);
        assertEquals(id, doctypeActual.getId());
        assertEquals(title, doctypeActual.getTitle());
    }

    @Test
    void doctypeMapperGetNullTest() {
        DoctypeDto doctypeDtoActual = doctypeMapper.doctypeToDoctypeDto(null);
        assertNull(doctypeDtoActual);

        Doctype doctypeActual = doctypeMapper.doctypeDtoToDoctype(null);
        assertNull(doctypeActual);
    }
}
