package gov.kui.docRepoR.service;

import gov.kui.docRepoR.domain.User;

public interface UserService {
    public User findByUsername(String username);
}
