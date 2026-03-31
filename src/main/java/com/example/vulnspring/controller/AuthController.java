package com.example.vulnspring.controller;

import com.example.vulnspring.UserRepository;
import com.example.vulnspring.entity.RefreshToken;
import com.example.vulnspring.entity.User;
import com.example.vulnspring.service.JwtService;
import com.example.vulnspring.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");


        if (isValidLogin(username, password)) {
            User user = new User();
            user.setUsername(username);

            String accessToken = jwtService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken.getToken());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).build();
    }


    private boolean isValidLogin(String username, String password) {
        if (username == null || password == null) return false;

        try {
            String query = "SELECT * FROM users WHERE USERNAME=\"" + username + "\" AND PASSWORD=\"" + password + "\"";
            Map<String, Object> result = jdbcTemplate.queryForMap(query);  // Inject JdbcTemplate
            return result.containsKey("username");
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        String refreshTokenStr = request.get("refreshToken");
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenStr);


        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(refreshToken.getUser().getUsername());
        String newAccessToken = jwtService.generateToken(refreshToken.getUser());

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", newRefreshToken.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> request) {
        String refreshTokenStr = request.get("refreshToken");
        RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenStr);
        refreshTokenService.revokeAllUserTokens(refreshToken.getUser().getUsername());
        return ResponseEntity.noContent().build();
    }
}
