package gov.kui.docRepoR.service;

import gov.kui.docRepoR.model.User;

public interface UserService {
    public User findByUsername(String username);
}
