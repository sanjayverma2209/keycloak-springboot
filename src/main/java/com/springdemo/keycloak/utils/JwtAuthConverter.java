package com.springdemo.keycloak.utils;

import com.springdemo.keycloak.model.JwtAuthConverterProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtAuthConverterProperties jwtAuthConverterProperties;

    public JwtAuthConverter(JwtAuthConverterProperties jwtAuthConverterProperties) {
        this.jwtAuthConverterProperties = jwtAuthConverterProperties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> grantedAuthorities =
                Stream.concat(
                        new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                        extractResourceRoles(source).stream()
                ).collect(Collectors.toSet());
        return new JwtAuthenticationToken(source, grantedAuthorities, getPrincipalName(source));
    }

    private String getPrincipalName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (Objects.nonNull(jwtAuthConverterProperties.getPrincipalAttribute()))
            claimName = jwtAuthConverterProperties.getPrincipalAttribute();
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        if (Objects.isNull(jwt.getClaim("resource_access")))
            return Set.of();
        Map<String, Object> resourceAccessMap = jwt.getClaim("resource_access");
        if (Objects.isNull(resourceAccessMap.get(jwtAuthConverterProperties.getResourceId())))
            return Set.of();
        Map<String, Object> resourceMap = (Map<String, Object>) resourceAccessMap.get(jwtAuthConverterProperties.getResourceId());
        Collection<String> resourceRoles = (Collection<String>) resourceMap.get("roles");
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
