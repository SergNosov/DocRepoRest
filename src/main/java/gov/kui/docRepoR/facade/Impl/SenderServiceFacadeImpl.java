package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.dto.mappers.SenderMapper;
import gov.kui.docRepoR.facade.SenderServiceFacade;
import gov.kui.docRepoR.service.SenderService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SenderServiceFacadeImpl implements SenderServiceFacade {

    private final SenderMapper senderMapper;
    private final SenderService senderService;

    @Autowired
    public SenderServiceFacadeImpl(SenderMapper senderMapper, SenderService senderService) {
        this.senderMapper = senderMapper;
        this.senderService = senderService;
    }

    @Override
    public List<SenderDto> findAll() {
        return senderService.findAllDtos();
    }

    @Override
    public SenderDto findById(int id) {
        return senderService.findDtoById(id);
    }

    @Override
    public SenderDto save(SenderDto senderDto) {
       Assert.notNull(senderDto, "Не указан senderDto (null)");
        senderDto.setId(0);
        return saveOrUpdate(senderDto);
    }

    @Override
    public SenderDto update(SenderDto senderDto) {
        Assert.notNull(senderDto, "Не указан senderDto (null)");
        return saveOrUpdate(senderDto);
    }

    @Override
    public int deleteById(int id) {
        int deletedId = senderService.deleteById(id);
        return deletedId;
    }

    private SenderDto saveOrUpdate(SenderDto senderDto){
        Sender sender = senderService.save(
                senderMapper.senderDtoToSender(senderDto)
        );

        Assert.notNull(sender, "Не указан sender (null)");
        return senderMapper.senderToSenderDto(sender);
    }
}
