package gov.kui.docRepoR.dto.mappers;

import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.SenderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface SenderMapper {
    public static SenderMapper INSTANCE = Mappers.getMapper(SenderMapper.class);
    public SenderDto senderToSenderDto(Sender sender);
    public Sender senderDtoToSender(SenderDto senderDto);
    public Set<SenderDto> sendersToSenderDtos(Set<Sender> senders);
    public Set<Sender> senderDtosToSenders(Set<SenderDto> senderDtos);
}
