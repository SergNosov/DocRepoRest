package gov.kui.docRepoR.ControllersTestMockMVC;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.JsonUser;
import gov.kui.docRepoR.config.security.JwtTokenUtil;
import gov.kui.docRepoR.controller.AuthenticationController;
import gov.kui.docRepoR.controller.RestExceptionHandler;
import gov.kui.docRepoR.domain.LoginUser;
import gov.kui.docRepoR.domain.DocRepoUser;
import gov.kui.docRepoR.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTestMockMVCStandalone {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    AuthenticationController authenticationController;

    private MockMvc mockMvc;
    private UsernamePasswordAuthenticationToken userPasswordToken;
    private LoginUser validLoginUser;

    @BeforeEach
    public void setUp() throws IOException {

        validLoginUser = new ObjectMapper().readValue(JsonUser.JSON_GOOD.toString(), LoginUser.class);
        userPasswordToken = new UsernamePasswordAuthenticationToken(
                validLoginUser.getUsername(),
                validLoginUser.getPassword(),
                Collections.emptyList()
        );

        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(Jackson2HttpMessage.MessageConverter())
                .build();
    }

    @Test
    public void testRegisterOk() throws Exception {

        given(authenticationManager.authenticate(any())).willReturn(userPasswordToken);

        Authentication auth = authenticationManager.authenticate(userPasswordToken);
        assertTrue(auth.isAuthenticated());

        DocRepoUser docRepoUser = new DocRepoUser();
        docRepoUser.setUsername(validLoginUser.getUsername());

        given(userService.findByUsername(any())).willReturn(docRepoUser);
        given(jwtTokenUtil.generateToken(any())).willReturn("newToken");

        String token = jwtTokenUtil.generateToken(docRepoUser);

        mockMvc.perform(post(DocRepoURL.TOKEN_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUser.JSON_GOOD.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.result.token", is(token)))
                .andExpect(jsonPath("$.result.username", is(docRepoUser.getUsername())));
    }

    @Test
    public void testRegisterBadCredentials() throws Exception {

        given(authenticationManager.authenticate(any())).willThrow(BadCredentialsException.class);

        mockMvc.perform(post(DocRepoURL.TOKEN_LOCALHOST.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUser.JSON_NO_PASSWORD.toString())
        )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", containsString("BadCredentialsException")));
    }
}
