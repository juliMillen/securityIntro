package com.jm.security.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    //Con estas configuraciones aseguramos la AUTENTICIDAD del token a crear

    @Value("${security.jwt.private.key}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;


    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        //esto está dentro del security context holder
        String username = authentication.getPrincipal().toString();

        //obtener los permisos/autorizaciones
        //traer los permisos separados por coma

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String token = JWT.create()
                .withIssuer(this.userGenerator)  //usuario que genera el token
                .withSubject(username) // a quien se le genera el token
                .withClaim("authorities", authorities) //claims son los datos contraidos en el jwt
                .withIssuedAt(new Date()) //fecha de generacio del token
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) //fecha de expiracion
                .withJWTId(UUID.randomUUID().toString()) // id al token - genera un random
                .withNotBefore(new Date(System.currentTimeMillis())) // desde cuando es valido(en este caso desde ahora)
                .sign(algorithm); //firma que creamos con la clave secreta
        return token;
    }

    public DecodedJWT validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodeJWT = verifier.verify(token);
            return decodeJWT;

        }catch (JWTVerificationException except){
            throw new JWTVerificationException("Invalid token. Not authorized");
        }
    }

    public String extractUsername(DecodedJWT decodeJWT) {
        //El subject es el usuario segun lo que establecimos al crear el token
        return decodeJWT.getSubject().toString();
    }

    //devuelvo un claim en particular
    public Claim getSpecificClaim(DecodedJWT decodeJWT, String claimName) {
        return decodeJWT.getClaim(claimName);
    }

    //devuelvo todos los claims
    public Map<String, Claim> returnAllClaims(DecodedJWT decodeJWT) {
        return decodeJWT.getClaims();
    }
}
