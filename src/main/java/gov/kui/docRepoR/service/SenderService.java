package gov.kui.docRepoR.service;

import gov.kui.docRepoR.Entity.Sender;
import gov.kui.docRepoR.validation.CheckValueIsExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SenderService extends CheckValueIsExists {

    /**
     * getting all senders
     *
     * @return all senders from DB
     */
    public List<Sender> findAll();

    /**
     * getting all senders (pagination)
     *
     * @return all senders from DB in page as a representation of the resource
     */
    public Page<Sender> findAllPage(Pageable pageable);

    /**
     * getting specify sender by ID
     *
     * @param id - sender's id for receiving
     * @return sender by id
     */
    public Sender findById(int id);

    /**
     * required for save or update sender to db
     *
     * @param sender - sender for saving or updating
     * @return sender
     */
    public Sender save(Sender sender);

    /**
     * delete sender  by ID
     *
     * @param id - sender's id for deleting
     * @return sender id
     */
    public int deleteById(int id);
}
