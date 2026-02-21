package com.jm.security.Security.Config.Filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jm.security.Utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
      String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

      if(jwtToken != null){
          jwtToken = jwtToken.substring(7); // 7 letras + 1 espacio
          DecodedJWT decodeJWT = jwtUtils.validateToken(jwtToken);

          //si es valido concedemos acceso
          String username = jwtUtils.extractUsername(decodeJWT);

          //me devuelve claim, pasarlo a string
          String authorities = jwtUtils.getSpecificClaim(decodeJWT, "authorities").asString();

          //si todo esta bien, setearlo en el Context Holder
          // convertilos a GrantedAuthority
          Collection authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

          //si se valida el token, le damos acceso al usuario en el context holder
          SecurityContext context = SecurityContextHolder.getContext();
          Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authoritiesList);
          context.setAuthentication(authentication);
          SecurityContextHolder.setContext(context);
      }
      //si no viene token, va al siguiente filtro
        // si no viene el token, arroja error
      filterChain.doFilter(request, response);
    }
}
