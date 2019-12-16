package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.model.User;
import gov.kui.docRepoR.dao.UserRepository;
import gov.kui.docRepoR.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Неверное имя пользхователя." +
                    " Пользователь не зарегистрирован.");
        }
        return user;
    }
}
