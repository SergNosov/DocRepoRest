package gov.kui.docRepoR.dto.mappers;

import gov.kui.docRepoR.domain.Document;
import gov.kui.docRepoR.dto.DocumentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DoctypeMapper.class, SenderMapper.class, FileEntityMapper.class} )
public interface DocumentMapper {
    public static DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);
    public DocumentDto documentToDocumentDto(Document document);
}
