package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zipgoo.server.dto.UserSignUpDto;
import zipgoo.server.repository.UserRepository;
import zipgoo.server.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        return ResponseEntity.ok("이메일, 닉네임, 나이, 생년월일");
    }



}
