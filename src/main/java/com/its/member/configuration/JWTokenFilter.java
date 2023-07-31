package com.its.member.configuration;

import com.its.member.service.MemberService;
import com.its.member.tools.JWToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JWTokenFilter extends OncePerRequestFilter {


    private final MemberService memberService;
    private final String secretKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization :{}",authorization );

        if(authorization ==null || !authorization.startsWith("Bearer ")){
            log.error("authorization을 잘못보냈습니다");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        //토큰 만료 여부
        if(JWToken.isExpired(token, secretKey)){
            log.error("토큰이 만료되었습니다. 다시 로그인 해주세요");
            System.out.println("토큰이 만료되었습니다. 다시 로그인 해주세요");
            filterChain.doFilter(request, response);
            return;
        }

        String Email = JWToken.getEmail(token, secretKey);
        log.info(Email);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(Email, null, List.of(new SimpleGrantedAuthority("USER")));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
