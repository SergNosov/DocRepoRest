package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.domain.User;
import gov.kui.docRepoR.dao.UserRepository;
import gov.kui.docRepoR.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return findOne(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findOne(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
    }

    public List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
    }

    private User findOne(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Неверное имя пользхователя." +
                    " Пользователь не зарегистрирован.");
        }
        return user;
    }
}
