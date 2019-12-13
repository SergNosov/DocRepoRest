package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.Entity.User;
import gov.kui.docRepoR.dao.UserRepository;
import gov.kui.docRepoR.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findOne(String username) {
        return userRepository.findByUsername(username);
    }
}
