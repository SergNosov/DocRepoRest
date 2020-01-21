package gov.kui.docRepoR.service;

import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.validation.CheckValueIsExists;

public interface DoctypeService extends BaseCrudService<Doctype>, CheckValueIsExists {
}
