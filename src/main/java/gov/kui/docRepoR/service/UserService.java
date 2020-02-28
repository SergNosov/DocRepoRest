package gov.kui.docRepoR.service;

import gov.kui.docRepoR.domain.DocRepoUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public DocRepoUser findByUsername(String username);
}
