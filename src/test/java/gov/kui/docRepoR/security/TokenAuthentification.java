package gov.kui.docRepoR.security;

import gov.kui.docRepoR.DocRepoURL;
import gov.kui.docRepoR.domain.ApiResponse;
import gov.kui.docRepoR.domain.AuthToken;
import gov.kui.docRepoR.domain.LoginUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.LinkedHashMap;

// only for tests !!!

public class TokenAuthentification {
    final static private int userId = 123;
    final static private String user="john";
    final static private String password="fun123";
    final static private  HttpHeaders headers = new HttpHeaders();

    static {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    public static String getToken(){
        return getAuthToken(8080).getToken();
    }

    public static String getToken(int portNumber){
        return getAuthToken(portNumber).getToken();
    }

    private static AuthToken getAuthToken(int portNumber) {
        AuthToken authToken = new AuthToken(userId,user,"");
        LoginUser loginUser = new LoginUser(user,password);

        try {
            ResponseEntity<ApiResponse> responseEntity = new RestTemplate().exchange(
                    "http://localhost:"+portNumber+ DocRepoURL.LOGIN,
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
