package zipgoo.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import zipgoo.server.domain.Role;
import zipgoo.server.domain.User;
import zipgoo.server.dto.LoginDto;
import zipgoo.server.dto.UserSignUpDto;
import zipgoo.server.exception.ErrorResponse;
import zipgoo.server.jwt.JwtService;
import zipgoo.server.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;


    public void signUp(UserSignUpDto userSignUpDto) throws Exception{
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        if(userRepository.findByNickname(userSignUpDto.getNickname()).isPresent()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            ErrorResponse errorResponse = new ErrorResponse(400, "이미 존재하는 닉네임 입니다.");
            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
            return;
        }


        String accessToken = jwtService.createAccessToken();
        String refreshToken = jwtService.createRefreshToken();

        jwtService.setAccessTokenHeader(response, accessToken);
        jwtService.setRefreshTokenHeader(response, refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);

        // 액세스 토큰으로 해당 sns api에서 정보 받아오는 코드 구현하고, 이메일, 닉네임, 생년월일 등 받아오기(아직 구현 안됨)

        User user = User.builder()
                .email("dummy@naver.com") // 임시 이메일
                .nickname(userSignUpDto.getNickname())
                //.birthDate("1999.07.15")
                .role(Role.USER)
                .refreshToken(refreshToken)
                .build();
        userRepository.save(user);
    }

    public void login(LoginDto loginDto) throws Exception{
        // 액세스 토큰과 리프레시 토큰 재발급해서 db에 해당되는 닉네임에 넣기.

    }
}