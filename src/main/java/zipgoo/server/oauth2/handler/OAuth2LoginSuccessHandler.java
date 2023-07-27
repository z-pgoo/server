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

    // 로그인하면 sns 타입이랑 sns에 맞는 액세스 토큰이 옴.
    // 그 액세스 토큰 카카오로 보내서 데이터 가져옴. 아마도 이메일,
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Login 성공");

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        log.info(String.valueOf(oAuth2User));

        String accessToken = jwtService.createAccessToken();
        String refreshToken = jwtService.createRefreshToken();


        // 프론트 단에 Response header를 통해서 전달
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 액세스토큰과 리프레시 토큰 생성해서 프론트단으로 보내기.
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken); //다시 생성된 리프레시 토큰으로 업데이트.

    }
}