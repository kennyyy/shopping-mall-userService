package com.mallproject.UserService.controller;


import com.mallproject.UserService.model.User;
import com.mallproject.UserService.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게스트 API", description = "로그인 인증 필요 없이 모든 유저가 사용할 수 있는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/guest")
public class GuestController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GuestService guestService;

    @Operation(summary = "회원가입", description = "유저 회원가입")
    @PostMapping("/join")
    public ResponseEntity<User> userJoin(@RequestBody User user){
        logger.info("가입유저 입력데이터 : " + user);
        return guestService.userJoin(user);
    }
    
    @Operation(summary = "홈", description = "테스트 홈")
    @GetMapping("/home")
    public String home(){
        return "hi!";
    }


}
