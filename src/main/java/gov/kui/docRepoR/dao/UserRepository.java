package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.domain.DocRepoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<DocRepoUser,Integer> {
    public Optional<DocRepoUser> findByUsername(String username);
}
