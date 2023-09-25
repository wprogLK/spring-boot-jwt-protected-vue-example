package ch.wproglk.example.web.authentication.controllers;

import ch.wproglk.example.web.authentication.payload.request.SignupRequest;
import ch.wproglk.example.web.authentication.payload.response.JwtResponse;
import ch.wproglk.example.data.role.Role;
import ch.wproglk.example.data.role.RoleDao;
import ch.wproglk.example.data.user.User;
import ch.wproglk.example.data.user.UserDao;
import ch.wproglk.example.service.jwt.Jwt;
import ch.wproglk.example.service.jwt.TokenService;
import ch.wproglk.example.service.user.UserDetails;
import ch.wproglk.example.web.authentication.payload.request.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController
{
    private final AuthenticationManager authenticationManager;

    private final UserDao userDao;
    private final RoleDao roleDao;

    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response)
    {
        JwtResponse jwt = getJwt(loginRequest.getUsername(), loginRequest.getPassword());
        addAuthCookie(jwt, response);

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signUpRequest, HttpServletResponse response) //TODO check validation
    {
        Map<String, String> validationErrors = new HashMap<>();

        if (userDao.existsByUsername(signUpRequest.getUsername()))
        {
            validationErrors.put("username", "Username already taken");
        }

        if (userDao.existsByEmail(signUpRequest.getEmail()))
        {
            validationErrors.put("email", "Email already in use");
        }

        if (!validationErrors.isEmpty())
        {
            return ResponseEntity.badRequest().body(validationErrors);
        }

        createUser(signUpRequest);

        JwtResponse jwt = getJwt(signUpRequest.getUsername(), signUpRequest.getPassword());
        addAuthCookie(jwt, response);

        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/signout")
    public void signout(HttpServletRequest request, HttpServletResponse response)
    {
        Cookie cookie = WebUtils.getCookie(request, "auth.access_token");

        if (cookie != null)
        {
            cookie.setValue(null);
            cookie.setMaxAge(0);

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
    }

    @GetMapping("/renew")
    public JwtResponse renewCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Cookie cookie = WebUtils.getCookie(request, "auth.access_token");
        if (cookie != null)
        {
            String token = cookie.getValue();
            Jwt jwt = tokenService.getJwt(token);

            return new JwtResponse(jwt);
        }

        response.sendError(HttpStatus.UNAUTHORIZED.value());
        signout(request, response);

        return null;
    }

    private void addAuthCookie(JwtResponse jwt, HttpServletResponse response)
    {
        // https://www.springcloud.io/post/2022-04/spring-samesite/#gsc.tab=0
        ResponseCookie cookie = ResponseCookie.from("auth.access_token", URLEncoder.encode(jwt.getAccessToken(), StandardCharsets.UTF_8))
                                              .httpOnly(true)
                                              .secure(true)
                                              .maxAge((int) TokenService.COOKIES_LIFESPAN.toSeconds())
                                              .path("/api")
                                              .sameSite("none") // currently needed as a hack
                                              //TODO domain
                                              .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private JwtResponse getJwt(String username, String password)
    {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenService.generateToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String[] roles = userDetails.getAuthorities()
                                    .stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .toArray(String[]::new);

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    private void createUser(SignupRequest signUpRequest)
    {
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (CollectionUtils.isEmpty(strRoles))
        {
            Role role = roleDao.findByName(ch.wproglk.example.model.Role.USER)
                               .orElseThrow(() -> new NoSuchElementException("Error: Role not found"));
            roles.add(role);
        }
        else
        {
            for (String role : strRoles)
            {
                switch (role)
                {
                    case "admin":
                        Role adminRole = roleDao.findByName(ch.wproglk.example.model.Role.ADMIN)
                                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleDao.findByName(ch.wproglk.example.model.Role.USER)
                                               .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            }
        }

        user.setRoles(roles);
        userDao.save(user);
    }
}
