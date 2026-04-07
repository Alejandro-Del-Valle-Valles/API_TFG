package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.enums.RolUsuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final long expSeconds;

    public JwtTokenService(JwtEncoder jwtEncoder,
                           @Value("${security.jwt.exp-seconds}") long expSeconds) {
        this.jwtEncoder = jwtEncoder;
        this.expSeconds = expSeconds;
    }

    /**
     * Crea tokens de acceso para los usuarios registrados
     * @param correoUsuario Correo de la cuenta
     * @param rol Rol de la cuenta
     * @return String con el token
     */
    public String createToken(String correoUsuario, RolUsuario rol) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expSeconds))
                .subject(String.valueOf(correoUsuario))
                .claim("rol", rol.name())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
