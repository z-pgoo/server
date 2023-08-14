package zipgoo.server.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import zipgoo.server.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import zipgoo.server.exception.ErrorResponse;
import zipgoo.server.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Jwt 인증 필터
 * "/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 *
 * 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
 * AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청
 *
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR
 * 3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR 방식)
 *                              인증 성공 처리는 하지 않고 실패 처리
 *
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login"; // "/login"으로 들어오는 요청은 Filter 작동 X

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    /**

     *** 프론트단에서 토큰을 보냈을 때 가장 먼저 실행되는 부분이 doFilterInternal ***

     1. 액세스 토큰을 보낸 경우
     먼저 토큰이 유효한지 판단한다.
     1-1. 만약 액세스 토큰이 만료됐을 경우에는 DB에 이메일로 접근해서 리프레시 토큰을 통해 액세스 토큰을 재발급한다.
     1-2. 만약 토큰이 유효한 경우에는 로그인 상태를 계속 유지해준다.

     2. 리프레시 토큰을 보낸 경우(1-1의 경우라고 볼 수 있다)
     이 경우는 액세스 토큰이 만료되어 액세스 토큰을 요청한 경우가 해당된다. 리프레시 토큰의 유효성을 검사한다.
     2-1. 만료된 경우에는 리프레시 토큰과 액세스 토큰을 모두 재발급해서 서버에 리프레시토큰 업데이트, 프론트단에는 액세스 토큰을 보내준다.
     2-2. 만료되지 않은 경우에는 액세스 토큰 재발급해서 프론트단에 보내준다. 만약 리프레시 토큰이 일정 시간(예를 들면 2일?)밖에 안 남은 경우에도 재발급해줄 수 있다.

     **/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authrizationHeader = request.getHeader(AUTHORIZATION);

        log.info("doFilterInternal() 호출");


        // /login과 /sign-up은 요청 헤더에 토큰이 존재하지 않으므로, 검사를 진행하지 않음.
        if(request.getRequestURI().equals(NO_CHECK_URL) || request.getRequestURI().contains("/random-name") ||request.getRequestURI().contains("/sign-up") || request.getRequestURI().contains("/search-dong") ){
            filterChain.doFilter(request, response);
            return ;
        }

        // 토큰이 유효하지 않은 경우에는 400에러.
        if(authrizationHeader == null || !authrizationHeader.startsWith(jwtService.BEARER))
        {
            log.info("JWT Token값이 존재하지 않습니다.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            ErrorResponse errorResponse = new ErrorResponse(400, "JWT Token이 존재하지 않습니다.");
            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
        }

        // 토큰이 적절히 들어온 경우
        // 먼저 액세스 토큰이 잘 들어왔는지 확인
        // 1. 적절하다면 OK

        // 2. 기간이 만료된 경우 리프레시 토큰 검사
        // 2-1. 리프레시 토큰 검사 결과 적절하다면 액세스 토큰 발급하고, 헤더에 리프레시 토큰 + 액세스 토큰 담아서 보내기. DB 리프레시 토큰 업데이트.
        // 2-2. 리프레시 토큰 검사 결과 만료 혹은 아예 없는 경우 오류 출력. 다시 로그인하기.

        // 3. 토큰 자체가 오류인 경우에는 400에러.
        else{
            try{
                String accessToken = jwtService.extractAccessToken(request).orElse(null);
                JWT.require(Algorithm.HMAC512(jwtService.getSecretKey())).build().verify(accessToken);
                log.info("액세스 토큰 유효함");

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("dummy", null);
                // username을 어떻게 해야할까.......

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
                // reponse 보내는 부분 수정하기.
            }
            catch (TokenExpiredException e){
                log.error("액세스 토큰 기간 만료 {}", e.getMessage());
                try{
                    String refreshToken = jwtService.extractRefreshToken(request).orElse(null);
                    JWT.require(Algorithm.HMAC512(jwtService.getSecretKey())).build().verify(refreshToken);
                    log.info("리프레시 토큰 유효함");

                    checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
                }
                catch (Exception ee){
                    log.error("리프레시 토큰 만료 or 잘못됨. 재로그인 필요");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("utf-8");
                    ErrorResponse errorResponse = new ErrorResponse(400, "잘못된 리프레시 토큰입니다.");
                    new ObjectMapper().writeValue(response.getWriter(), errorResponse);
                }

            }
            catch (Exception e){
                log.error("유효하지 않은 액세스 토큰 {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ErrorResponse errorResponse = new ErrorResponse(400, "잘못된 액세스 토큰입니다.");
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);

            }


        }


    }

    /**
     *  [액세스 토큰/리프레시 토큰 재발급 메소드]
     *  파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로
     *  JwtService.createAccessToken()으로 AccessToken 생성,
     *  reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
     *  그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
     */
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        log.info("리프레시 유효한지 확인 후 적절하면 액세스 토큰 생성.");
        userRepository.findByRefreshToken(refreshToken)
                .ifPresentOrElse(
                        user -> {
                            String newRefreshToken = reIssueRefreshToken(user);
                            // 리프레시 토큰이 DB에 있는지 확인하고, 만약에 있다면 리프레시 토큰 재발급해서 DB에 넣는다.
                            // 그 다음에 액세스 토큰을 다시 만들고, 프론트단에 액세스토큰을 보내준다.
                            jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(), newRefreshToken);
                        },
                        () -> {
                            // 리프레시 토큰이 유효하지 않은 경우에 처리할 로직
                            log.error("리프레시 토큰이 유효하지 않습니다.");
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.setContentType(APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("utf-8");
                            ErrorResponse errorResponse = new ErrorResponse(400, "리프레시 토큰이 DB에 존재하지 않습니다.");

                            try {
                                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    /**
     * [리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드]
     * jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후
     * DB에 재발급한 리프레시 토큰 업데이트 후 Flush
     */
    private String reIssueRefreshToken(User user) {
        log.info("reIssueRefreshToken() 호출");
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }



}
