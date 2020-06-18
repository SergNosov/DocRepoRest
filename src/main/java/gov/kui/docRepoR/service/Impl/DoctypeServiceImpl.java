package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dao.DoctypeRepository;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.service.DoctypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DoctypeServiceImpl implements DoctypeService {
    private final DoctypeRepository doctypeRepository;

    @Autowired
    public DoctypeServiceImpl(DoctypeRepository doctypeRepository) {
        this.doctypeRepository = doctypeRepository;
    }

    @Override
    public List<Doctype> findAll() {//todo убрать лишние методы из BaseCrudService interface
        return doctypeRepository.findAll();
    }

    @Override
    public Doctype findById(int id) {
        return doctypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден тип документа с id - " + id));
    }

    @Override
    public DoctypeDto findDtoById(int id) {
        return doctypeRepository.findDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден тип документа с id - " + id));
    }

    @Override
    public List<DoctypeDto> findAllDtos() {
        return doctypeRepository.findAllDtos();
    }

    @Override
    @Transactional
    public Doctype save(Doctype doctype) {
        Assert.notNull(doctype, "Не указан doctype (null)");
        Assert.hasText(doctype.getTitle(), "Заголовок (doctype.title) пуст. doctype: " + doctype);

        if (doctype.getId() != 0 && !doctypeRepository.existsById(doctype.getId())) {
            throw new IllegalArgumentException("Не найден тип документа с id - " + doctype.getId());
        }
        return doctypeRepository.save(doctype);
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        Doctype doctype = this.findById(id);
        doctypeRepository.delete(doctype);
        return id;
    }

    public boolean isExistsValueInField(Object value, String fieldName) {
        if (value == null || fieldName == null) {
            return false;
        }
        if (!fieldName.equals("title")) {
            throw new UnsupportedOperationException("Validation on field: '" + fieldName + "' not supported.");
        }
        return this.doctypeRepository.existsByTitle(value.toString());
    }
}
