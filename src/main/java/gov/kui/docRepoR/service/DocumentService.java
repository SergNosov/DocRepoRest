package gov.kui.docRepoR.service;

import gov.kui.docRepoR.Entity.Document;

import java.util.List;

public interface DocumentService {

    /**
     * getting all document
     *
     * @return all document from DB
     */
    public List<Document> findAll();

    /**
     * getting specify documentDto by ID
     *
     * @param id - document's id for receiving
     * @return document by id
     */
    public Document findById(int id);

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
     * @return id document
     */
    public int deleteById(int id);
}
