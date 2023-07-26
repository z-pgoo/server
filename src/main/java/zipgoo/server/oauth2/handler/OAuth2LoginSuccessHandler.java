package zipgoo.server.oauth2.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import zipgoo.server.domain.User;
import zipgoo.server.jwt.JwtService;
import zipgoo.server.domain.Role;
import zipgoo.server.oauth2.CustomOAuth2User;
import zipgoo.server.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
//@Transactional
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // 최초 로그인 부분
            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트

            if(oAuth2User.getRole() == Role.GUEST) {
                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                String refreshToken = jwtService.createRefreshToken();
                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
                response.sendRedirect("oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트, *주소 수정하기.
                User findUser = userRepository.findByEmail(oAuth2User.getEmail())
                        .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
                findUser.authorizeUser();
                findUser.setRefreshToken(refreshToken);
                userRepository.saveAndFlush(findUser);

                jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); //프론트 단으로 액세스 토큰, 리프레시 토큰 보내기.

            } else {
                loginSuccess(response, oAuth2User); // 최초 로그인이 아닌 경우. 즉, 리프레시 토큰과 액세스 토큰이 모두 만료된 경우에 다시 로그인하는 경우라고 보면 된다.
            }
        } catch (Exception e) {
            throw e;
        }

    }

    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();


        // 프론트 단에 Response header를 통해서 전달
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 액세스토큰과 리프레시 토큰 생성해서 프론트단으로 보내기.
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken); //다시 생성된 리프레시 토큰으로 업데이트.

    }
}

