package auth.proximity.authservice.security.jwt;

public class JwtConstants {

    public static final long EXPIRE_ACCESS_TOKEN = 30 * 60 * 1000; //5 * 60 * 1000 = 30 minutes

    public static final long EXPIRE_REFRESH_TOKEN = 120 * 60 * 1000;

    public static final String ISSUER = "PROXIMITY";

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTH_HEADER = "Authorization";
}
