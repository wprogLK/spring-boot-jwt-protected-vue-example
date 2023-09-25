package ch.wproglk.example.service.jwt;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Jwt
{
    String token;
    Long id;
    String username;
    String email;
    String[] roles;
}