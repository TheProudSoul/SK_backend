package cn.theproudsoul.sk.filter;

import cn.theproudsoul.sk.web.exception.UnauthorizedException;
import cn.theproudsoul.sk.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    public JWTFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // TODO 在token里存放其他user信息，作为验证需要
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String path = req.getRequestURI();
        log.info("{} {} {}", req.getMethod(), path, req.getHeader("Content-Type"));
        // 无需验证token
        if (path.contains("/account")
                || path.contains("/images")
                || (path.contains("/retrieve"))) {
            chain.doFilter(req, res);
            return;
        }

        final String authorization = req.getHeader("Authorization");
        String jwtToken;
        long userId;
        if (authorization != null && authorization.startsWith("Super Knowledge ")) {
            jwtToken = authorization.substring(16);
            try {
                userId = Long.parseLong(jwtTokenUtil.getUserIDFromToken(jwtToken));
            } catch (IllegalArgumentException e) {
                throw new UnauthorizedException("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                throw new UnauthorizedException("JWT Token has expired");
            }
        } else {
            throw new UnauthorizedException("JWT Token does not begin with Super Knowledge String");
        }

        // Once we get the token validate it.
        if (userId > 0) {
            // if token is valid configure Spring Security to manually set
            // authentication
            req.setAttribute("user", userId);
        }
        chain.doFilter(req, res);
    }
}
