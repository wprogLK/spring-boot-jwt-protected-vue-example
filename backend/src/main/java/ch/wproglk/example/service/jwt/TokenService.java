package ch.wproglk.example.service.jwt;

import ch.wproglk.example.service.user.UserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService
{
    public static final Duration COOKIES_LIFESPAN = Duration.ofMinutes(5);
    private static final String CLAIM_KEY_ID = "id";
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_EMAIL = "email";
    private static final String CLAIM_KEY_SCOPE = "scope";

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public String generateToken(Authentication authentication)
    {
        Instant now = Instant.now();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                                          .issuer("self")
                                          .issuedAt(now)
                                          .expiresAt(now.plus(COOKIES_LIFESPAN))
                                          .subject(authentication.getName())
                                          .claim(CLAIM_KEY_ID, userDetails.getId())
                                          .claim(CLAIM_KEY_USERNAME, userDetails.getUsername())
                                          .claim(CLAIM_KEY_EMAIL, userDetails.getEmail())
                                          // authority claim name "scope" based on JwtAuthenticationConverter default value
                                          .claim(CLAIM_KEY_SCOPE, StringUtils.join(userDetails.getAuthorities(), " "))
                                          .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Jwt getJwt(String token)
    {
        var jwt = decoder.decode(token);
        
        return Jwt.builder()
                  .token(jwt.getTokenValue())
                  .id(jwt.getClaim(CLAIM_KEY_ID))
                  .username(jwt.getClaim(CLAIM_KEY_USERNAME))
                  .email(jwt.getClaim(CLAIM_KEY_EMAIL))
                  .roles(((String) jwt.getClaim(CLAIM_KEY_SCOPE)).split(" "))
                  .build();
    }
}