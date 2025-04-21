package com.mallproject.UserService.security;

import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthrizationFilter extends BasicAuthenticationFilter {

    private final JWTService jwtService;

    private final List<String> permitURIs = Arrays.asList("guest", "public", "env", "login");

    public JwtAuthrizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        for(String permituri : permitURIs){
            //모든 요청은 이 필터를 거친다
            //JWT 검증이 필요없는 URI는 제외시켜주자
            if(requestURI.contains(permituri)){
                filterChain.doFilter(request, response);
                return;
            }
        }

        String headers = request.getHeader("Authorization");

        //JWT 인증 확인
        if(headers == null || !headers.startsWith("Bearer ")) {
            response.setContentType("application/json; charset=UTF8;");
            response.sendError(403, "토큰이 없습니다.");
            return;
        }

        //토큰 유효성 검사
        try {
            String token = headers.substring( 7 ); //Bearer공백

            if(jwtService.isTokenValid(token)) {
                //JWT에서 설정해둔 Claim정보 가져오기
                String cUserid = jwtService.getClaimUserId(token);
                String cUserRule = jwtService.getClaimUserRole(token);

                logger.info("JWT에서 가져온 claim 정보" + "id : " + cUserid + "role : " + cUserRule);

                User user = User.builder()
                                .userId(cUserid)
                                .role(cUserRule).build();
                //권한 확인을 위한 인증객체 설정 및 저장
                MyUserDetails myUserDetails = new MyUserDetails(user);
                Authentication auth = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                //다음필터로 연결
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            response.setContentType("application/json; charset=UTF8;");
            response.sendError(403, "토큰이 유효하지 않습니다.");
        }

    }
}
