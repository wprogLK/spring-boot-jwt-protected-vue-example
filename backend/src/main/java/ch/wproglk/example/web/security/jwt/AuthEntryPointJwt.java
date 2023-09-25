package ch.wproglk.example.web.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

// handles for example the case when the given jwt token is not longer valid. (see NimbuisJwtDecoder#validateJwt -> JwtValidationException)
@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint
{
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException
    {
        log.error("Unauthorized error: message {}", authenticationException.getMessage());
        log.debug("", authenticationException);


        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        // invalidate cookie -> see signout
        // TODO: move to cookie helper class
        Cookie cookie = WebUtils.getCookie(request, "auth.access_token");

        if (cookie != null)
        {
            cookie.setValue(null);
            cookie.setMaxAge(0);

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

    }
}
