package gov.kui.docRepoR.service;

import gov.kui.docRepoR.Entity.Doctype;
import gov.kui.docRepoR.validation.CheckValueIsExists;

import java.util.List;

public interface DoctypeService extends CheckValueIsExists {
    /**
     * getting all doctype
     *
     * @return all doctype from DB
     */
    public List<Doctype> findAll();

    /**
     * getting specify doctype by ID
     *
     * @param id - doctype's id for receiving
     * @return doctype by id
     */
    public Doctype findById(int id);

    /**
     * required for save or update doctype to DB
     *
     * @param doctype - doctype for saving or updating
     * @return doctype
     */
    public Doctype save(Doctype doctype);

    /**
     * delete doctype  by ID
     *
     * @param id - doctype's id for deleting
     * @return id
     */
    public int deleteById(int id);
}
