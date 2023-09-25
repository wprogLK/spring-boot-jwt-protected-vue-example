package ch.wproglk.example.web.authentication.payload.response;

import ch.wproglk.example.service.jwt.Jwt;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class JwtResponse
{
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String type = "Bearer";
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String token;
    private Long id;
    private String username;
    private String email;
    private String[] roles;

    public JwtResponse(Jwt jwt)
    {
        this(jwt.getToken(), jwt.getId(), jwt.getUsername(), jwt.getEmail(), jwt.getRoles());
    }

    public JwtResponse(String token, Long id, String username, String email, String[] roles)
    {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken()
    {
        return token;
    }

    public void setAccessToken(String accessToken)
    {
        this.token = accessToken;
    }

    public String getTokenType()
    {
        return type;
    }

    public void setTokenType(String tokenType)
    {
        this.type = tokenType;
    }
}
