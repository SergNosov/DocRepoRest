package gov.kui.docRepoR.config.security;

public class Constants {
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 30*60;//30 min = 1 800 sec
    public static final String SIGNING_KEY = "kuiChita123r!";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER_STRING = "Authorization";

    private Constants(){}
}
