package edu.mobiledev.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.fasterxml.jackson.databind.*;
import edu.mobiledev.dto.*;
import lombok.extern.log4j.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.stereotype.*;

@Component
@Log4j2
public class AccessTokenEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    )
        throws IOException, ServletException {
        log.error("Произошла ошибка аутентификации", authException);

        ApiExceptionDto re = new ApiExceptionDto("Произошла ошибка авторизации", null);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, re);
        responseStream.flush();
    }

}
