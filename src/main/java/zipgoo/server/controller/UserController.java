package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zipgoo.server.dto.UserSignUpDto;
import zipgoo.server.service.UserService;
import zipgoo.server.utils.RandomNameGenerator.RandomNameGeneratorImpl;
import zipgoo.server.utils.RandomNameGenerator.RandomNameGeneratorUtil;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RandomNameGeneratorUtil randomNameGenerator;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        userService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

    @PostMapping("/random-name")
    public String randName(){
        return randomNameGenerator.getFullName();
    }
    @GetMapping("/jwt-test")
    public String jwtTest(){
        return "jwtTest 요청 성공";
    }
}
