package gov.kui.docRepoR.service;

import gov.kui.docRepoR.model.Sender;
import gov.kui.docRepoR.validation.CheckValueIsExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SenderService extends BaseCrudService<Sender>, CheckValueIsExists {

    /**
     * getting all senders (pagination)
     *
     * @return all senders from DB in page as a representation of the resource
     */
    public Page<Sender> findAllPage(Pageable pageable);
}
