package gov.kui.docRepoR.service;

import gov.kui.docRepoR.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public User findByUsername(String username);
}
