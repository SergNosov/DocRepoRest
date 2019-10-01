package gov.kui.docRepoR.service;

import gov.kui.docRepoR.Entity.Document;
import gov.kui.docRepoR.dto.DocumentDto;

import java.util.List;

public interface DocumentService {

    /**
     * getting all document
     *
     * @return all document from DB
     */
    public List<DocumentDto> findAll();

    /**
     * getting specify documentDto by ID
     *
     * @param id - documentDto's id for receiving
     * @return documentDto by id
     */
    public DocumentDto findById(int id);

    /**
     * required for save or update document to db
     *
     * @param document - document for saving or updating
     * @return document
     */
    public Document save(Document document);

    /**
     * delete document  by ID
     *
     * @param id - document's id for deleting
     */
    public void deleteById(int id);
}
