package gov.kui.docRepoR.dto.mappers;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dto.DoctypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DoctypeMapper {
    public static DoctypeMapper INSTANCE = Mappers.getMapper(DoctypeMapper.class);
    public DoctypeDto doctypeToDoctypeDto(Doctype doctype);
    public Doctype doctypeDtoToDoctype(DoctypeDto doctypeDto);
}
