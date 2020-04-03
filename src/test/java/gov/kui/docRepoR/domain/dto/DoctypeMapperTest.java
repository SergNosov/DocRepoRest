package gov.kui.docRepoR.domain.dto;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.mappers.DoctypeMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DoctypeMapperTest {
    private final int id = 1;
    private final String title = "ЗаголовокТипаДокумента";
    private final DoctypeMapper doctypeMapper = DoctypeMapper.INSTANCE;

    @Test
    void doctypeToDoctypeDtoTest(){
        Doctype doctype = new Doctype();
        doctype.setId(id);
        doctype.setTitle(title);

        DoctypeDto doctypeDto = doctypeMapper.doctypeToDoctypeDto(doctype);

        assertEquals(id,doctypeDto.getId());
        assertEquals(title,doctypeDto.getTitle());
    }

    @Test
    void doctypeToDoctypeDtoNullTest(){
        DoctypeDto doctypeDto = doctypeMapper.doctypeToDoctypeDto(null);
        assertNull(doctypeDto);
    }
}
