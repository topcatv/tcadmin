package org.topcatv.devops.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.topcatv.devops.model.User;
import org.topcatv.devops.security.CustomUserDetails;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.header}")
    private String header;

    public JwtLoginFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.warn("JwtLoginFilter attemptAuthentication user {}, pwd {}", username, password);
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.warn("JwtLoginFilter successfulAuthentication");
        // Credentials was protected, it is null
        CustomUserDetails user = (CustomUserDetails) authResult.getPrincipal();
        String token = jwtTokenUtil.generateToken(user);
        response.addHeader(header, tokenHead + " " + token);
        responseText(response, objectResult(token));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.warn("JwtLoginFilter unsuccessfulAuthentication");
        responseText(response, errorMessage(failed.getMessage()));
    }

    private void responseText(HttpServletResponse response, String content) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.flushBuffer();
    }

    private String objectResult(Object object) {
        JSONObject root = new JSONObject();
        root.put("success", true);
        root.put("data", object);
        return root.toString();
    }

    private String errorMessage(String message) {
        JSONObject root = new JSONObject();
        root.put("success", false);
        root.put("message", message);
        return root.toString();
    }
}