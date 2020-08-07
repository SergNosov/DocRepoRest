package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.ApiResponse;
import gov.kui.docRepoR.domain.AuthToken;
import gov.kui.docRepoR.domain.LoginUser;
import gov.kui.docRepoR.domain.DocRepoUser;
import gov.kui.docRepoR.config.security.JwtTokenUtil;
import gov.kui.docRepoR.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4201")
@RequestMapping("/token")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserService userService,
                                    JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/generate-token")
    // todo выпилить ApiResponse....
      public AuthToken register(@RequestBody LoginUser loginUser) throws AuthenticationException {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );

        final DocRepoUser docRepoUser = userService.findByUsername(auth.getName());
        final String token = jwtTokenUtil.generateToken(docRepoUser);

        return new AuthToken(docRepoUser.getId(),docRepoUser.getUsername(),token);
    }
}
