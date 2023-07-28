package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zipgoo.server.domain.User;
import zipgoo.server.dto.LoginDto;
import zipgoo.server.dto.UserSignUpDto;
import zipgoo.server.jwt.JwtService;
import zipgoo.server.repository.UserRepository;
import zipgoo.server.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        return userService.signUp(userSignUpDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) throws Exception{
        return "로그인 성공 !";
    }



}
