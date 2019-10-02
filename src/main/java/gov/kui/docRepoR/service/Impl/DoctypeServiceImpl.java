package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.Entity.Doctype;
import gov.kui.docRepoR.dao.DoctypeRepository;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.service.DoctypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctypeServiceImpl implements DoctypeService {
    private DoctypeRepository doctypeRepository;
    private DoctypeDto doctypeDto;

    @Autowired
    public DoctypeServiceImpl(DoctypeRepository  doctypeRepository, DoctypeDto doctypeDto){
        this.doctypeRepository = doctypeRepository;
        this.doctypeDto = doctypeDto;
    }

    @Override
    public List<DoctypeDto> findAll() {
        List<Doctype> doctypes = doctypeRepository.findAll();
        List<DoctypeDto> doctypeDtos = new ArrayList<>();

        for (Doctype doctype: doctypes){
            doctypeDtos.add(doctypeDto.getDoctypeDto(doctype));
        }

        return doctypeDtos;
    }

    @Override
    public DoctypeDto findById(int id) {
        Optional<Doctype> result = doctypeRepository.findById(id);

        if (result.isPresent()) {
            return doctypeDto.getDoctypeDto(result.get());
        } else {
            throw new RuntimeException("Did not find doctype id - "+id);
        }
    }

    @Override
    public Doctype save(Doctype doctype) {
        System.err.println("DoctypeServiceImpl Doctype:"+doctype);
        doctypeRepository.save(doctype);
        return doctype;
    }

    @Override
    public void deleteById(int id) {
        doctypeRepository.deleteById(id);
    }

    @Override
    public List<Doctype> findByTitle(String type){
        return doctypeRepository.findByTitle(type);
    }

    public boolean isExistsValueInField(Object value, String fieldName){
        if (value==null || fieldName==null){
            return false;
        }
        if (!fieldName.equals("type")){
            throw new UnsupportedOperationException("Validation on field: '"+fieldName+"' not supported.");
        }
        return this.doctypeRepository.existsByTitle(value.toString());
    }
}
