package edu.mobiledev.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.validation.constraints.*;

import edu.mobiledev.jwt.*;
import edu.mobiledev.model.*;
import edu.mobiledev.service.*;
import lombok.extern.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.web.authentication.*;
import org.springframework.web.filter.*;

@Log4j2
public class AccessTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String accessToken = authHeader.substring(7);
            if (jwtHelper.validateAccessToken(accessToken) != null) {
                String userId = jwtHelper.getUserIdFromAccessToken(accessToken);
                User user = userService.getUserById(Long.parseLong(userId));
                UsernamePasswordAuthenticationToken upat =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        } catch (Exception e) {
            log.error("Произошла ошибка аутентификации", e);
        }
        filterChain.doFilter(request, response);
    }

}
