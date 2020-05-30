package cn.theproudsoul.sk.filter;

import cn.theproudsoul.sk.web.exception.UnauthorizedException;
import cn.theproudsoul.sk.web.result.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, res);
        } catch (UnauthorizedException e){
            setErrorResponse(HttpStatus.UNAUTHORIZED, res, "Invalid token.");
        }
        catch (JwtException e) {
            setErrorResponse(HttpStatus.BAD_REQUEST, res, "Invalid token format.");
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, res,"Unexpected exception during validate token.");
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, String message){
        response.setStatus(status.value());
        response.setContentType("application/json");
        final ExceptionResponse bodyOfResponse = new ExceptionResponse(message);
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(bodyOfResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
