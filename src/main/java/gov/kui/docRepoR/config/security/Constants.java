package gov.kui.docRepoR.config.security;

public class Constants {
    //public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 30*60;//30*60 = 1 800 sec (30 min)
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 10*60;//10*60 = 600 sec (10 min)
    public static final String SIGNING_KEY = "kuiChita123r!";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER_STRING = "Authorization";

    private Constants(){}
}
