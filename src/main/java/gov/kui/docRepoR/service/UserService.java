package gov.kui.docRepoR.service;

import gov.kui.docRepoR.Entity.User;

public interface UserService {
    public User findOne(String username);
}
