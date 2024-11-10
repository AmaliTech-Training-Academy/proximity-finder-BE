package gateway.proximity.gatewayserver.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationPreFilter extends AbstractGatewayFilterFactory<AuthenticationPreFilter.Config> {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    private final RouteValidator validator;
    private final ObjectMapper objectMapper;

    public AuthenticationPreFilter(RouteValidator validator, ObjectMapper objectMapper) {
        super(Config.class);
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders requestHeaders = request.getHeaders();
            String token = requestHeaders.getFirst(HttpHeaders.AUTHORIZATION);
            if (validator.isSecured.test(exchange.getRequest())) {
                if (token == null || !token.startsWith("Bearer ")) {
                    return handleAuthError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
                }
                token = token.substring(7);

                try {

                    Claims claims = Jwts.parser()
                            .verifyWith((SecretKey) getSignKey())
                            .build().parseSignedClaims(token)
                            .getPayload();

                    String email = claims.getSubject();
                    String role = claims.get("role").toString();
                    String username = claims.get("username").toString();

                    exchange.getRequest().mutate()
                            .header("email", email)
                            .header("role", role)
                            .header("username", username)
                            .build();
                    return chain.filter(exchange);
                } catch (Exception e) {
                    return handleAuthError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        });
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Mono<Void> handleAuthError(ServerWebExchange exchange, String message, HttpStatus status){
        ServerHttpResponse response =  exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        responseBody.put("message", message);
        responseBody.put("status", status.value());
        responseBody.put("errorCode", status.value());

        try{
            byte [] bytes = objectMapper.writeValueAsBytes(responseBody);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Config {

    }
}
