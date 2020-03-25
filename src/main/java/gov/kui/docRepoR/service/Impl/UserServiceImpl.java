package gov.kui.docRepoR.service.Impl;

import gov.kui.docRepoR.domain.DocRepoUser;
import gov.kui.docRepoR.dao.UserRepository;
import gov.kui.docRepoR.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public DocRepoUser findByUsername(String username) {
        return findOne(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        DocRepoUser docRepoUser = findOne(username);
        return new org.springframework.security.core.userdetails.User(docRepoUser.getUsername(),
                docRepoUser.getPassword(),
                getAuthority()
        );
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));// todo брать роли из БД
    }

    private DocRepoUser findOne(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                ()->new UsernameNotFoundException("Неверное имя пользхователя. Пользователь не зарегистрирован.")
        );
    }
}
