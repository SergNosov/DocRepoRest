package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.mappers.DoctypeMapper;
import gov.kui.docRepoR.facade.DoctypeServiceFacade;
import gov.kui.docRepoR.service.DoctypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctypeServiceFacadeImpl implements DoctypeServiceFacade {

    private final DoctypeService doctypeService;
    private final DoctypeMapper doctypeMapper;

    @Autowired
    public DoctypeServiceFacadeImpl(DoctypeService doctypeService, DoctypeMapper doctypeMapper) {
        this.doctypeService = doctypeService;
        this.doctypeMapper = doctypeMapper;
    }

    @Override
    public List<DoctypeDto> findAll() {
        List<DoctypeDto> doctypeDtos = new ArrayList<>();
        List<Doctype> doctypes = doctypeService.findAll();

        if (doctypes != null) {
            doctypeDtos = doctypeMapper.doctypesToDoctypeDtos(doctypes);
        }
        return doctypeDtos;
    }

    @Override
    public DoctypeDto findById(int id) {
        Doctype doctype = doctypeService.findById(id);
        if (doctype == null) {
            throw new RuntimeException("doctype from doctypeService is null. id:"+id);
        }
        DoctypeDto doctypeDto = doctypeMapper.doctypeToDoctypeDto(doctype);
        return doctypeDto;
    }

    @Override
    public DoctypeDto save(DoctypeDto doctypeDto) {
        Assert.notNull(doctypeDto, "Не указан doctypeDto (null)");
        doctypeDto.setId(0);
        return saveOrUpdate(doctypeDto);
    }

    @Override
    public DoctypeDto update(DoctypeDto doctypeDto) {
        Assert.notNull(doctypeDto, "Не указан doctypeDto (null)");
        return saveOrUpdate(doctypeDto);
    }

    @Override
    public int deleteById(int id) {
        int deletedId = doctypeService.deleteById(id);
        return deletedId;
    }

    private DoctypeDto saveOrUpdate(DoctypeDto doctypeDto){
        Doctype doctype = doctypeService.save(
                doctypeMapper.doctypeDtoToDoctype(doctypeDto)
        );

        Assert.notNull(doctype, "Не указан doctype (null)");
        return doctypeMapper.doctypeToDoctypeDto(doctype);
    }
}
