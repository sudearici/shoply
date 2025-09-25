package com.sudearici.demo;

import com.sudearici.demo.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/auth";
    }

    @Test
    void registerAndLoginShouldReturnToken() {
        // 1. Register user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(baseUrl() + "/register", user, String.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 2. Login
        Map<String, String> loginRequest = Map.of(
                "username", "testuser",
                "password", "password123"
        );
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(baseUrl() + "/login", loginRequest, Map.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).containsKey("token");

        // 3. Use token to call protected endpoint
        String token = (String) loginResponse.getBody().get("token");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> protectedResponse =
                restTemplate.exchange("http://localhost:" + port + "/api/orders", HttpMethod.GET, entity, String.class);

        assertThat(protectedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
