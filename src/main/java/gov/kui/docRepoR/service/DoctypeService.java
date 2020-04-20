package gov.kui.docRepoR.service;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.validation.CheckValueIsExists;

import java.util.List;

public interface DoctypeService extends BaseCrudService<Doctype>, CheckValueIsExists {

    public List<DoctypeDto> findAllDtos();
    public DoctypeDto findDtoById(int id);
}
