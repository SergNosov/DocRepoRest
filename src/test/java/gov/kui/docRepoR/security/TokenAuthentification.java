package gov.kui.docRepoR.security;

import gov.kui.docRepoR.model.ApiResponse;
import gov.kui.docRepoR.model.AuthToken;
import gov.kui.docRepoR.model.LoginUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.LinkedHashMap;

public class TokenAuthentification {
    final static private String user="john";
    final static private String password="fun123";
    final static private  HttpHeaders headers = new HttpHeaders();

    static {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    public static AuthToken getAuthToken() {
        AuthToken authToken = new AuthToken("",user);
        LoginUser loginUser = new LoginUser(user,password);

        try {
            ResponseEntity<ApiResponse> responseEntity = new RestTemplate().exchange(
                    "http://localhost:8080/token/generate-token",
                    HttpMethod.POST,
                    new HttpEntity<>(loginUser, headers),
                    ApiResponse.class
            );

            ApiResponse apiResponse = responseEntity.getBody();

            if (apiResponse.getStatus() == HttpStatus.OK.value()) {
                String token = ((LinkedHashMap) apiResponse.getResult()).get("token").toString();
                authToken.setToken(token);
            }

        } catch (HttpClientErrorException hcee){
            System.err.println("Неверное имя пользователя или пароль. ");
            throw new RuntimeException("Неверное имя пользователя или пароль. " + hcee.getMessage());
        }

        return authToken;
    }
}
