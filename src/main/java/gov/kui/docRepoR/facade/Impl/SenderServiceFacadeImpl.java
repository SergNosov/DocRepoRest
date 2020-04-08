package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.dto.mappers.SenderMapper;
import gov.kui.docRepoR.facade.SenderServiceFacade;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public SenderDto findById(int id) {
        return null;
    }

    @Override
    public SenderDto save(SenderDto object) {
        return null;
    }

    @Override
    public SenderDto update(SenderDto object) {
        return null;
    }

    @Override
    public int deleteById(int id) {
        return 0;
    }
}
