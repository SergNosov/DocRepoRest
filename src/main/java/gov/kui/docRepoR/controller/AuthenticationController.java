package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.Entity.ApiResponse;
import gov.kui.docRepoR.Entity.AuthToken;
import gov.kui.docRepoR.Entity.LoginUser;
import gov.kui.docRepoR.Entity.User;
import gov.kui.docRepoR.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private AuthenticationManager authenticationManager;
    private UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/generate-token")
    public ApiResponse<AuthToken> register(@RequestBody LoginUser loginUser) throws AuthenticationException {

        System.out.println("user name from request: "+ loginUser.getUsername());
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        final User user = userService.findOne(loginUser.getUsername());
       // final String token = jwtTokenUtil.generateToken(user);
        //return new ApiResponse<>(200, "success",new AuthToken(token, user.getUsername()));

        System.out.println("user name from DB: "+ user.getUsername());
        return null;
    }
}
