package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.config.security.JwtTokenUtil;
import gov.kui.docRepoR.domain.ApiResponse;
import gov.kui.docRepoR.domain.AuthToken;
import gov.kui.docRepoR.domain.DocRepoUser;
import gov.kui.docRepoR.domain.LoginUser;
import gov.kui.docRepoR.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static gov.kui.docRepoR.config.security.Constants.AUTH_HEADER_STRING;
import static gov.kui.docRepoR.config.security.Constants.TOKEN_PREFIX;

@RestController
@CrossOrigin(origins = "http://localhost:4201")
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

    @PostMapping("/login")
      public ApiResponse<AuthToken> register(@RequestBody LoginUser loginUser) throws AuthenticationException {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );

        final DocRepoUser docRepoUser = userService.findByUsername(auth.getName());
        final String authToken = jwtTokenUtil.generateToken(docRepoUser);

        return new ApiResponse<AuthToken>(HttpStatus.OK.value(),
                HttpStatus.OK.toString(),
                "success",
                new AuthToken(docRepoUser.getId(),docRepoUser.getUsername(),authToken));
    }

    @GetMapping("/login/check")
    public ApiResponse<AuthToken> checkToken(HttpServletRequest request){

        final String header = request.getHeader(AUTH_HEADER_STRING);

        if (header==null || !header.startsWith(TOKEN_PREFIX)) {
            throw new BadCredentialsException("AuthenticationController: checkToken() is failed.");
        }

        final String authToken = header.replace(TOKEN_PREFIX, "");
        final String username = jwtTokenUtil.getUsernameFromToken(authToken);
        final DocRepoUser docRepoUser = userService.findByUsername(username);

        return new ApiResponse<AuthToken>(HttpStatus.OK.value(),
                HttpStatus.OK.toString(),
                "success",
                new AuthToken(docRepoUser.getId(),docRepoUser.getUsername(),authToken));
    }
}
