package hr.aspira.careapp.backend.filer;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CharsetHttpFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(httpServletResponse.getStatus() == 200) {
            httpServletResponse.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
