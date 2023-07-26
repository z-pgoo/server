package zipgoo.server.controller;

import lombok.RequiredArgsConstructor;
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
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        userService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

    @GetMapping("/jwt-test")
    public String jwtTest(){
        return "jwtTest 요청 성공";
    }

    //SNS 로그인 이후 추가 정보 입력, 지금은 닉네임 값만 입력하도록 설정해둠.
    @PostMapping("/ssign-up")
    public String ssignUp(@RequestParam("nickname") String nickname) throws Exception{
        userService.ssignUp(nickname);

        userRepository.findById(userRepository.findMaxUserId())
                .ifPresent(user -> {
                    user.setNickname(nickname);
                    userRepository.saveAndFlush(user);
                });
        return "추가 회원가입 성공";
    }
}
