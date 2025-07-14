package gift.auth;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public class JwtAuthenticationFilter implements Filter {

    private final JwtProvider jwtProvider;

    private final String HEADER_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(HEADER_PREFIX)) {
            String token = authHeader.replaceFirst(HEADER_PREFIX, "");
            try {
                if (jwtProvider.validateToken(token)) {
                    String email = jwtProvider.getEmail(token);

                    httpRequest.setAttribute("email", email);
                } else {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getWriter().write("Invalid JWT token");
                    return;
                }
            } catch (Exception e) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("JWT token error: " + e.getMessage());
                return;
            }
        }
        chain.doFilter(httpRequest, httpResponse);
    }
}
