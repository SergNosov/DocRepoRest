package gov.kui.docRepoR.ControllersTestMockMVC;

import gov.kui.docRepoR.controller.AuthenticationController;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.domain.User;
import gov.kui.docRepoR.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.constraints.AssertTrue;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTestMockMVCStandalone {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    AuthenticationController authenticationController;

    private MockMvc mockMvc;
    private UsernamePasswordAuthenticationToken userPasswordToken;

    @BeforeEach
    public void setUp() {

        List<GrantedAuthority> grantedAuths =
                AuthorityUtils.commaSeparatedStringToAuthorityList("EMPLOYEE");
        userPasswordToken =
                new UsernamePasswordAuthenticationToken("john", "fun123", grantedAuths);

        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter())
                .build();
    }

    @Test
    public void testRegisterOk() {

//        given(authenticationManager.authenticate(any())).willReturn(userPasswordToken);
//        assertTrue(authenticationManager.authenticate(null).isAuthenticated());

        User user = new User();
        user.setUsername("john");


    }

}
