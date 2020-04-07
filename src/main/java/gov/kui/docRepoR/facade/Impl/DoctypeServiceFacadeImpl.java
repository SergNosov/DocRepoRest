package gov.kui.docRepoR.facade.Impl;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.dto.mappers.DoctypeMapper;
import gov.kui.docRepoR.facade.DoctypeServiceFacade;
import gov.kui.docRepoR.service.DoctypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Doctype> doctypes = doctypeService.findAll();
        List<DoctypeDto> doctypeDtos = doctypeMapper.doctypesToDoctypeDtos(doctypes);

        if (doctypeDtos == null) {
            throw new RuntimeException("doctypeDtos == null.");
        }

        return doctypeDtos;
    }

    @Override
    public DoctypeDto findById(int id) {
        DoctypeDto doctypeDto = doctypeMapper.doctypeToDoctypeDto(
                doctypeService.findById(id)
        );

        if (doctypeDto == null) {
            throw new RuntimeException("Для указанного значения id: " + id + ". doctypeDto = null;");
        }
        return doctypeDto;
    }

    @Override
    public DoctypeDto save(DoctypeDto object) {
        return null;
    }

    @Override
    public DoctypeDto update(DoctypeDto object) {
        return null;
    }

    @Override
    public int deleteById(int id) {
        return 0;
    }
}
