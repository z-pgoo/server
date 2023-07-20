package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zipgoo.server.dto.UserSignUpDto;
import zipgoo.server.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        userService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

    @GetMapping("/jwt-test")
    public String jwtTest(){
        return "jwtTest 요청 성공";
    }
}
