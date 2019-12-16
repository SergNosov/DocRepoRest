package gov.kui.docRepoR.dao;

import gov.kui.docRepoR.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User,Integer> {
    public User findByUsername(String username);
}
