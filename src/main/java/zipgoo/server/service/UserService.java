package zipgoo.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zipgoo.server.domain.Role;
import zipgoo.server.domain.User;
import zipgoo.server.dto.UserSignUpDto;
import zipgoo.server.repository.UserRepository;

// 자체 로그인 회원 가입시 사용하는 회원 가입 API 로직
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpDto userSignUpDto) throws Exception{
        if(userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if(userRepository.findByNickname(userSignUpDto.getNickname()).isPresent()){
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .nickname(userSignUpDto.getNickname())
                .age(userSignUpDto.getAge())
                .role(Role.USER)
                .build();
        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }

    public void ssignUp(String nickname) throws Exception{
        if(userRepository.findByNickname(nickname).isPresent()){
            throw new Exception("이미 존재하는 닉네임입니다.");
        }
        userRepository.findMaxUserId();
    }
}