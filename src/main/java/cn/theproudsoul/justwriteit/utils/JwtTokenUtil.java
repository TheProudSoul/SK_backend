package cn.theproudsoul.justwriteit.utils;

import cn.theproudsoul.justwriteit.persistence.model.UserModel;
import cn.theproudsoul.justwriteit.web.exception.InvalidTokenException;
import cn.theproudsoul.justwriteit.web.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5h

    @Value("${jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getUserIDFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        try{
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (SignatureException e){
            throw new UnauthorizedException("Token format incorrect.");
        }
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserModel userModel) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userModel.getId());
    }

    // while creating the token -
    // 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //    compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, long subject) {

        return Jwts.builder().setClaims(claims).setSubject(String.valueOf(subject)).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // validate token
    public static void validateToken(HttpServletRequest request, long user) {
        long userId = (long) request.getAttribute("user");
        if (userId!=user)throw new InvalidTokenException("INVALID_TOKEN");
    }
}
