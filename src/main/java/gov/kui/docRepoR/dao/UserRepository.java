package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.DocRepoUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<DocRepoUser,Integer> {
    public DocRepoUser findByUsername(String username);
}
