package gov.kui.docRepoR.dto.mappers;

import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.SenderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SenderMapper {
    public static SenderMapper INSTANCE = Mappers.getMapper(SenderMapper.class);
    public SenderDto senderToSenderDto(Sender sender);
}
