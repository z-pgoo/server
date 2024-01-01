package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zipgoo.server.domain.User;
import zipgoo.server.dto.LoginDto;
import zipgoo.server.dto.UserSignUpDto;
import zipgoo.server.jwt.JwtService;
import zipgoo.server.repository.UserRepository;
import zipgoo.server.service.UserService;
import zipgoo.server.service.response.CommonResponse;
import zipgoo.server.service.response.ResponseService;
import zipgoo.server.utils.RandomNameGenerator.NameDto;
import zipgoo.server.utils.RandomNameGenerator.RandomNameGeneratorImpl;
import zipgoo.server.utils.RandomNameGenerator.RandomNameGeneratorUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final RandomNameGeneratorUtil randomNameGenerator;
    private final ResponseService responseService;

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        log.info("회원가입 요청 시작 User : {}", userSignUpDto.toString());
        return userService.signUp(userSignUpDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) throws Exception{
        log.info("로그인 요청 시작 User : {}", loginDto);
        return userService.login(loginDto);
    }
    @GetMapping("/random-name")
    public CommonResponse<NameDto> randName(){
        return responseService.getCommonResponse(randomNameGenerator.getFullName());
    }

}
