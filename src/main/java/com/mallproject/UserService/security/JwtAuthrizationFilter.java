package com.mallproject.UserService.security;

import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class JwtAuthrizationFilter extends BasicAuthenticationFilter {

    private UserMapper userMapper;

    private final List<String> permitURIs = Arrays.asList("/guest","/home", "/public");

    public JwtAuthrizationFilter(AuthenticationManager authenticationManager, UserMapper userMapper) {
        super(authenticationManager);
        this.userMapper = userMapper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        for(String permituri : permitURIs){
            //JWT 검증이 필요없는 URI는 제외
            if(requestURI.startsWith(permituri)){
                filterChain.doFilter(request, response);
                return;
            }
        }

        String headers = request.getHeader("Authorization");

        //JWT 인증 확인
        if(headers == null || !headers.startsWith("Bearer ")) {
            response.setContentType("text/plain; charset=UTF8");
            response.sendError(403, "토큰이 없습니다.");
            return;
        }

        //토큰 유효성 검사
        try {
            String token = headers.substring( 7 ); //Bearer공백
            boolean result = JWTService.isTokenValid(token);

            if(result) {
                //JWT에서 설정해둔 Claim정보 가져오기
                String username = JWTService.getClaimUserName(token);

                User user = userMapper.findUser(username);
                logger.info("JWT에서 가져온 유저정보" + user);

                //권한 확인을 위한 인증객체 설정 및 저장
                MyUserDetails myUserDetails = new MyUserDetails(user);
                Authentication auth = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                //다음필터로 연결
                filterChain.doFilter(request, response);
            } else {
                response.setContentType("text/html; charset=UTF8;");
                response.sendError(403, "토큰이 만료되었습니다");

            }
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF8;");
            response.sendError(403, "토큰이 위조되었습니다");
        }

    }
}
