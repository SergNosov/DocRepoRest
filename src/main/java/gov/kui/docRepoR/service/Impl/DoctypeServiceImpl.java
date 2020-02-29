package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dao.DoctypeRepository;
import gov.kui.docRepoR.service.DoctypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctypeServiceImpl implements DoctypeService {
    private final DoctypeRepository doctypeRepository;

    @Autowired
    public DoctypeServiceImpl(DoctypeRepository doctypeRepository) {
        this.doctypeRepository = doctypeRepository;
    }

    @Override
    public List<Doctype> findAll() {
        return doctypeRepository.findAll();
    }

    @Override
    public Doctype findById(int id) {
        return doctypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Не найден тип документа с id - " + id));
    }

    @Override
    public Doctype save(Doctype doctype) {
        if (!(doctype == null || doctype.getTitle() == null || doctype.getTitle().trim().isEmpty())) {
            if (doctype.getId() != 0) {
                this.findById(doctype.getId());
            }
            return doctypeRepository.save(doctype);
        } else {
            throw new IllegalArgumentException("Не указан Doctype (null), или заголовок (doctype.title) пуст.");
        }
    }

    @Override
    public int deleteById(int id) {
        doctypeRepository.deleteById(this.findById(id).getId());
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
